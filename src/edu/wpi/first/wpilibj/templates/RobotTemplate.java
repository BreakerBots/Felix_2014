/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project. Final code revision for FRC TEAM 5014. @author PSCA119        */
/*----------------------------------------------------------------------------*/

/*
@ new programmers:
Please read my comments in the 2014 (this) FRC [Felix v1.1] robot
code for information on how FRC should be managed! I hope this
is helpful.
*/

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.DriverStationLCD;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Timer;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory. (WPILIB)
 */
public class RobotTemplate extends IterativeRobot {

    Console console = new Console();
    public Timer autonomousTimer;
    public DriverStationLCD LCD;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
        System.out.println("[INFO] ****** ROBOT IS READY FOR USE ******");
        // Tell programmer(s)/Console reader that the Robot is initialized
        // and will now call console.init(); and then run the loop when the
        // VM initialization is complete.
        console.init();
        // clears LCD Driver Station.
        LCD = DriverStationLCD.getInstance();
        LCD.clear();

    }

    public void disabledInit() {

    }

    public void autonomousInit() {
        console.autoInit();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {
        console.autoRun();
    }

    public void teleopInit() {
        LCD.clear();
        console.teleopInit();
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        // Run console instance for Console to call method run();
        // "Main loop" (while running); NOTE, FMS WILL AUTOMATICALLY DO THIS.
        console.run();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {

    }

    public void disabledPeriodic() {
        console.disabled();
    }
}
