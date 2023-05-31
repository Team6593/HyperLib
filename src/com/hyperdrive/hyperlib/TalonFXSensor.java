/* Copyright (c) 2023 HyperDrive Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.hyperdrive.hyperlib;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFXConfiguration;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;

/**
 * The TalonFXSensor class is a utility class that provides methods for interfacing with the
 * integrated sensor on a TalonFX motor controller. The class is designed to simplify the process of
 * working with TalonFX motor controllers and their integrated sensors,
 */
public class TalonFXSensor {
    /**
     * Creates a TalonFXConfiguration and sets the feedback sensor to the TalonFX's integrated
     * sensor
     */
    public TalonFXSensor() {
        final TalonFXConfiguration configuration = new TalonFXConfiguration();
        configuration.primaryPID.selectedFeedbackSensor = FeedbackDevice.IntegratedSensor;
    }

    static double FalconUnitsPerRevolution = 2048;

    /**
     * @param talonFX - the TalonFX motor controller
     * @return the current sensor position reported by the TalonFX motor controller
     */
    public static double getTalonFXSensorPosition(WPI_TalonFX talonFX) {
        return talonFX.getSelectedSensorPosition();
    }

    /**
     * @param talonFX - the TalonFX motor controller
     * @param sensorPosition - the sensor position read by the motor controller, this will not force
     *     the motor to drive to that motor position.
     */
    public static void setTalonFXSensorPosition(WPI_TalonFX talonFX, double sensorPosition) {
        talonFX.setSelectedSensorPosition(sensorPosition);
    }

    /**
     * resets TalonFX integrated sensor position to zero.
     *
     * @param talonFX - the TalonFX motor controller
     */
    public static void resetTalonFXSensorPosition(WPI_TalonFX talonFX) {
        talonFX.setSelectedSensorPosition(0);
    }

    /**
     * @param talonFX - the TalonFX motor controller
     * @return rotations made
     */
    public static double getRotations(WPI_TalonFX talonFX) {
        double sensorPosition = talonFX.getSelectedSensorPosition();
        double rotations = sensorPosition / FalconUnitsPerRevolution;
        return rotations;
    }

    /**
     * @param talonFX - the TalonFX motor controller
     * @return rotations-per-minute
     */
    public double getRotationsPerMinute(WPI_TalonFX talonFX) {
        double sensorVelocity = talonFX.getSelectedSensorVelocity();
        double rpm = sensorVelocity / FalconUnitsPerRevolution * 10;
        return rpm;
    }

    /**
     * @param talonFX - the TalonFX motor controller
     * @return rotations-per-second
     */
    public static double getRotationsPerSecond(WPI_TalonFX talonFX) {
        double sensorVelocity = talonFX.getSelectedSensorVelocity();
        double rps = sensorVelocity / FalconUnitsPerRevolution * 10;
        return rps;
    }

    /**
     * This function should be called in a periodic method or the execute() mehod in a subsystem
     * class. The motor specified in the parameters will drive forward or backward until it reaches
     * it's defined destination.
     *
     * @param talonFX - the TalonFX motor controller
     * @param encoderDistance - the distance read by the integrated sensor on the TalonFX, should be
     *     a whole number
     * @param speed - speed at which the motor will move, lower speed is more precise
     * @return true, if the motor successfully drives to it's destination, false if it does not
     */
    public static boolean driveToDistance(
            WPI_TalonFX talonFX, double encoderDistance, double speed) {

        // check if motor position is less than distance, if so then drive forward
        // if the encoder distance is negative,
        // then we'll check if the motor position is greater than the distance,
        // if so, we drive backwards

        double currentMotorPosition =
                Math.floor(talonFX.getSelectedSensorPosition()); // round down to nearest int

        if (currentMotorPosition < encoderDistance) {
            talonFX.set(ControlMode.PercentOutput, speed);
        } else if (encoderDistance < 0) {
            if (currentMotorPosition > encoderDistance) {
                talonFX.set(ControlMode.PercentOutput, -speed);
            } else {
                talonFX.stopMotor();
                return true;
            }
        } else {
            talonFX.stopMotor();
            return true;
        }

        return false; // default return value if none of the conditions are met
    }
}
