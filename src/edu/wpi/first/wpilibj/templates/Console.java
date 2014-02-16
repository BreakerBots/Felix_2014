package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

public class Console {

    //1 Compressor
    //2 Wormgear
    //3 Eltoro opener thingy at the top of the thing.
    public Talon motorLeft;
    public Talon motorRight;
    public RobotDrive robotdrive;
    public Talon elToro1;
    public Talon elToro2;
    public Compressor compressor;
    public Relay wormgear;
    //public Relay something
    public Joystick joystick;
    public Joystick joystick2;
    public Solenoid cock;
    public Solenoid uncock;
    public Solenoid lock;
    public Solenoid fire;
    public AnalogPotentiometer pot;
    public static Ultrasonic sonic;
    public Timer autonomousTimer;
    public Timer t;
    public DriverStationLCD LCD;
    public int autoState;
    public double autoTimeValue;

    public void init() {

        motorLeft = new Talon(1);
        motorRight = new Talon(2);
        System.out.println("[INFO] TALON[1|2]: Created!");
        robotdrive = new RobotDrive(motorLeft, motorRight);
        robotdrive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        robotdrive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);

        elToro1 = new Talon(3);
        elToro2 = new Talon(4);
        System.out.println("[INFO] TALON[3|4]: Created!");

        compressor = new Compressor(1, 1); // presureSwitchDigitalInput, RelayOut
        compressor.start();

        wormgear = new Relay(2);
        System.out.println("[INFO] RELAY[1,2,3]: Created!");

        joystick = new Joystick(1);
        joystick2 = new Joystick(2);
        System.out.println("[INFO] JOYSTICK[1|2]: Created!");

        cock = new Solenoid(1);
        uncock = new Solenoid(2);
        lock = new Solenoid(3);
        fire = new Solenoid(4);

        System.out.println("[INFO] Digital I/O: Enabled.");

        sonic = new Ultrasonic(4, 2);  // TODO: FIX NUMBERS pingDigOut, echoDigIn
        sonic.setEnabled(true);
        pot = new AnalogPotentiometer(2, 1000);

        autonomousTimer = new Timer();
        t = new Timer();

        LCD = DriverStationLCD.getInstance();

        System.out.println("[INFO] Robot Initialized");
    }

    public void run() {

        //robotdrive.arcadeDrive(joystick, true);
        robotdrive.tankDrive(joystick2, joystick);

        if (joystick.getRawButton(6)) {
            int distance = (int) (18.0 * 12 - sonic.getRangeInches());
            if (distance == 1) {
            }

        } else if (joystick.getRawButton(11)) {
            double error = 18.0 * 12 - sonic.getRangeInches();
            double ucommand = 0.5 * error;
            robotdrive.tankDrive(ucommand, ucommand);
            LCD.println(DriverStationLCD.Line.kUser3, 1, "US " + sonic.getRangeInches() + " " + ucommand);
            LCD.updateLCD();
        }

        if (joystick.getRawButton(5)) {
            wormgear.set(Relay.Value.kForward);
        } else if (joystick.getRawButton(4)) {
            wormgear.set(Relay.Value.kReverse);
        } else {
            wormgear.set(Relay.Value.kOff);
        }

        if (joystick.getRawButton(1)) {
            t.start();
            fire.set(false);
            lock.set(true);
            uncock.set(false);
            cock.set(true);
        }
        if (t.get() > 1.0 && t.get() < 1.5) {
            uncock.set(true);
            cock.set(false);
            System.out.println("[INFO] @Shooter@: Ready!");
        }

        if (t.get() > 2.0 && t.get() < 2.5) {
            System.out.println("[INFO] @Shooter@: FIRE!");
            fire.set(true);
            lock.set(false);
            uncock.set(true);
        }
        if (t.get() > 3.0) {
            fire.set(false);
            lock.set(true);
            cock.set(false);
            uncock.set(true);
            System.out.println("[INFO] @Shooter@: Please wait 2 seconds before another fire!");
            t.stop();
            t.reset();
        }

        if (joystick.getRawButton(6)) {
            double anumber = 18.0 * 12 - sonic.getRangeInches();
            System.out.println("[INFO] Distance: " + anumber + " ***");
        }

        if (joystick.getRawButton(3) == true) {
            elToro1.set(-1.0);
            elToro2.set(1.0);
        } else if (joystick.getRawButton(2) == true) {
            elToro1.set(1.0);
            elToro2.set(-1.0);
        } else {
            elToro1.set(0.0);
            elToro2.set(0.0);
        }

    }

    public void teleopInit() {

    }

//////////////////////////////////////////////////////////////////////////////
///                              AUTO CODE                                  //
//////////////////////////////////////////////////////////////////////////////
    public void autoInit() {
        autonomousTimer.reset();
        autonomousTimer.start();
        autoState = 1;
        sonic.setAutomaticMode(true);
    }

    public void autoRun() {

        double currentTime = autonomousTimer.get();
        double speedLeft = 0;
        double speedRight = 0;
        double distance;  // in feet
        distance = sonic.getRangeInches() * 12; //Sets distance to feet from inches

        if (autoState == 1) {
            if (distance > 17.75 && distance < 18.25) {
                autoState = 2;
                autoTimeValue = autonomousTimer.get();
            } else {  // we move to 18 feet
                double error = 18.0 * 12 - sonic.getRangeInches();
                double ucommand = 0.5 * error; // if the robot moves the wrong way, make this negative
                if (ucommand > 0.25) {
                    ucommand = 0.25;
                }
                if (ucommand < -0.25) {
                    ucommand = -0.25;
                }
                robotdrive.tankDrive(ucommand, ucommand);
            }
        }

        if (autoState == 2) {

            t.start();
            fire.set(false);
            lock.set(true);
            uncock.set(false);
            cock.set(true);
        }
        if (t.get() > 1.0 && t.get() < 1.5) {
            uncock.set(true);
            cock.set(false);
            System.out.println("[INFO] @Auto-Shooter@: Automode enabled, Prepairing.");
        }

        if (t.get() > 2.0 && t.get() < 2.5) {
            System.out.println("[INFO] @Auto-Shooter@: Shooting.");
            fire.set(true);
            lock.set(false);
            uncock.set(true);
        }
        if (t.get() > 3.0) {
            fire.set(false);
            lock.set(true);
            cock.set(false);
            uncock.set(true);
            System.out.println("[INFO] @Auto-Shooter@: ");
            t.stop();
            t.reset();
        }

        if ((autonomousTimer.get() - autoTimeValue) > 2.0) { // wait 2 seconds
            autoState = 3;
        }
        if (autoState == 3) {

            //Reset Pistons.
            //Reset Dev.
            //Set state off.   
        }

        /*
         3 18ft zones
         auto= ping sonic, measure distance to goal
         aim
         shoot
         move forward
         fin
         */
        /*If distance >= 18ft, move forward
         If distance <=18 ft,move backward
         If distance ==18, stop
         Write the above comments as PID*/
        /* if (currentTime <= 1.0) { /*move after shoot (or before to aim)
         speedLeft = 0.9*currentTime;
         speedRight = 0.9*currentTime;
         } else {
         speedLeft = 0;
         speedRight = 0;
         }
         */
        robotdrive.tankDrive(speedRight, speedLeft);

        LCD.clear();
        LCD.println(DriverStationLCD.Line.kUser1, 1, "Time: " + currentTime);
        LCD.updateLCD();

    }

    public void setShooterAngle(double voltage) {
        double error = voltage - pot.get();
        double ucommand = 0.5 * error; // if the robot moves the wrong way, make this negative
        if (ucommand > 0.25) {
            wormgear.set(Relay.Value.kForward);
        } else if (ucommand < -0.25) {
            wormgear.set(Relay.Value.kReverse);
        } else {
            wormgear.set(Relay.Value.kOff);
        }
    }

    public void disabled() {
        LCD.println(DriverStationLCD.Line.kUser2, 1, "POT: " + pot.get());
        LCD.updateLCD();
    }

}
