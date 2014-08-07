package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.AnalogChannel;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;

public class Console {
    
    /**
     * 
     * FELIX the robot, version 1.1 final.
     * Modified template.
     * 
     * 
     * Robot Code 2014 FRC Team #5104
     * @author PSCA119
     * 
     * 
     */ 
    
    public Talon motorLeft;
    public Talon motorRight;
    public RobotDriveSteering robotdrive;
    public Talon elToro1;
    public Talon elToro2;
    public Compressor compressor;
    public Relay wormgear;
    public Relay spreader;
    //public Relay something
    public Joystick joystick;
    // public Joystick joystick2;
    public Solenoid cock;
    public Solenoid uncock;
    public Solenoid lock;
    public Solenoid fire;
    public AnalogPotentiometer pot;
    public static Ultrasonic sonic;
    public AnalogChannel sonic2;
    public Timer autonomousTimer;
    public Timer t;
    public DriverStationLCD LCD;
    public int autoState;
    public double autoTimeValue;
    public static boolean anthonyIsDriving = false;
    public DriverStation ds;
    
    public void init() {
        
        motorLeft = new Talon(1);
        motorRight = new Talon(2);
        System.out.println("[INFO] TALON[1&2]: Created!");
        elToro1 = new Talon(3);
        elToro2 = new Talon(4);
        System.out.println("[INFO] TALON[3&4]: Created!");
        robotdrive = new RobotDriveSteering(motorLeft, motorRight);
        robotdrive.setInvertedMotor(RobotDriveSteering.MotorType.kRearLeft, true);
        robotdrive.setInvertedMotor(RobotDriveSteering.MotorType.kRearRight, true);
        
        compressor = new Compressor(1, 1); // presureSwitchDigitalInput, RelayOut
        compressor.start();
        
        wormgear = new Relay(2);
        spreader = new Relay(3);
        System.out.println("[INFO] RELAY[1&2&3]: Created!");
        
        joystick = new Joystick(1);
        //joystick2 = new Joystick(2);
        System.out.println("[INFO] JOYSTICK[1&2]: Created!");
        
        cock = new Solenoid(1);
        uncock = new Solenoid(2);
        lock = new Solenoid(3);
        fire = new Solenoid(4);
        
        System.out.println("[INFO] Digital I/O: Enabled.");
        
        sonic = new Ultrasonic(4, 2);
        sonic.setEnabled(true);
        sonic2 = new AnalogChannel(3);
        pot = new AnalogPotentiometer(2, 10);
        
        autonomousTimer = new Timer();
        t = new Timer();
        
        LCD = DriverStationLCD.getInstance();
        ds = DriverStation.getInstance();
        
        System.out.println("[INFO] Robot Initialized");
    }
    
    public void run() {
        
        anthonyIsDriving = !ds.getDigitalIn(1);
        
        robotdrive.arcadeDrive(joystick, true);

        if (joystick.getRawButton(6) && pot.get() <= 13.5) {
            wormgear.set(Relay.Value.kForward);
        } else if (joystick.getRawButton(7) && pot.get() >= 5.01) {
            wormgear.set(Relay.Value.kReverse);
        } else {
            wormgear.set(Relay.Value.kOff);
        }
        
        if (joystick.getRawButton(5)) {
            cock.set(true);
            uncock.set(false);
        } else {
            cock.set(false);
            uncock.set(true);
        }
        
        if (joystick.getRawButton(1)) {
            lock.set(false);
            fire.set(true);
        } else {
            lock.set(true);
            fire.set(false);
        }
        
        if (joystick.getRawButton(11) && ds.getDigitalIn(2)) {
            spreader.set(Relay.Value.kForward);
        } else if (joystick.getRawButton(10) && ds.getDigitalIn(2)) {
            spreader.set(Relay.Value.kReverse);
        } else {
            spreader.set(Relay.Value.kOff);
        }
        
        //double error = 3.0 * 12 - sonic.getRangeInches();
        //double ucommand = 0.5 * error;
        double R = (100 * (motorRight.getSpeed()));
        double L = (100 * (motorLeft.getSpeed()));
        
        LCD.clear();
        LCD.println(DriverStationLCD.Line.kUser1, 1, "UltraSonic: " + sonic2.getVoltage() / ((double) 5.0 / 1024.0) * (1.0 / 2.54));
        LCD.println(DriverStationLCD.Line.kUser2, 1, "Potentiometer: " + pot.get());
        LCD.println(DriverStationLCD.Line.kUser3, 1, "Speed(RM): " + R + " %");
        LCD.println(DriverStationLCD.Line.kUser4, 1, "Speed(LM): " + L + " %");
        LCD.println(DriverStationLCD.Line.kUser6, 1, ":   DS-UI: Felix[Ver]1.1");
        LCD.updateLCD();
        
        /**
         * El toro mech
         * 
         */
        if (joystick.getRawButton(3) == true) {
            elToro1.set(-0.75);
            elToro2.set(0.75);
        } else if (joystick.getRawButton(2) == true) {
            elToro1.set(0.75);
            elToro2.set(-0.75);
        } else {
            elToro1.set(0.0);
            elToro2.set(0.0);
        }
        
    }

    //****** OLD SHOOTER MECH *******//
    /**
     * if (joystick.getRawButton(1)) { t.start(); fire.set(false);
     * lock.set(true); uncock.set(false); cock.set(true); } if (t.get() > 1.0 &&
     * t.get() < 1.5) { uncock.set(true); cock.set(false);
     * System.out.println("[INFO] @Shooter@: Ready!"); }
     *
     * if (t.get() > 2.0 && t.get() < 2.5) { System.out.println("[INFO]
     * @Shooter@: FIRE!"); fire.set(true); lock.set(false); uncock.set(true); }
     * if (t.get() > 3.0) { fire.set(false); lock.set(true); cock.set(false);
     * uncock.set(true); System.out.println("[INFO] @Shooter@: Please wait 2
     * seconds before another fire!"); t.stop(); t.reset(); }    
     *
     */
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

    /**
     * Steps to perform in autonomous - Drive for set amount of time - Expand
     * the shooter - Align distance to about 3 feet
     */
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
                double ucommand = 0.5 * error; // if the robot moves the wrong way, make this 
                if (ucommand > 0.25) {
                    ucommand = 0.25;
                }
                if (ucommand < -0.25) {
                    ucommand = -0.25;
                }
                System.out.println("Error: " + error);
                System.out.println("Ucommand: " + ucommand);
                robotdrive.tankDrive(ucommand, ucommand);
            }
        }
        
        if (autoState == 2) {
            
            robotdrive.tankDrive(speedRight, speedLeft);
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
