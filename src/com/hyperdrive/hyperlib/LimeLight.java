/* Copyright (c) 2023 HyperDrive Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.hyperdrive.hyperlib;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimeLight {

    /**
     * Creates a new LimeLight
     *
     * @param limelightName - the name of your limelight (check limelight.local:5801 in your
     *     web-browser)
     */
    public LimeLight(String limelightName) {
        tableName = limelightName;
    }

    String tableName;

    /**
     * changes the table name of the limelight, as reported by the NetworkTables API
     *
     * @param name - the table name of the limelight
     */
    public void setLimeLightName(String name) {
        tableName = name;
    }

    NetworkTable table = NetworkTableInstance.getDefault().getTable(tableName);
    NetworkTableEntry validTargets = table.getEntry("tv"); // 0 or 1
    NetworkTableEntry tx =
            table.getEntry("tx"); // horizontal offset from crosshair to target (-27-27)
    NetworkTableEntry ty =
            table.getEntry("ty"); // Vertical offset from crosshair to target (-20.5-20.5)
    NetworkTableEntry ta = table.getEntry("ta");

    /**
     * @param limelightMountAngleDegrees how many degrees back is your limelight rotated from
     *     perfectly vertical? If the limelight is upside down, be sure to set the orientation to
     *     'upside-down' in Limelight finder-tool or local:5801,
     * @param limelightLensHeightInches distance from the center of the Limelight lens to the floor
     * @param goalHeightInches distance from the target to the floor
     * @return distance from the limelight to the target in inches
     */
    public double estimateDistance(
            double limelightMountAngleDegrees,
            double limelightLensHeightInches,
            double goalHeightInches) {
        NetworkTable _table = NetworkTableInstance.getDefault().getTable("limelight");
        NetworkTableEntry _ty = _table.getEntry("ty");
        double targetOffsetAngle_Vertical = _ty.getDouble(0.0);

        double angleToGoalDegrees = limelightMountAngleDegrees + targetOffsetAngle_Vertical;
        double angleToGoalRadians = angleToGoalDegrees * (3.14159 / 180.0);
        // calculate distance
        double distanceFromLimelightToGoalInches =
                (goalHeightInches - limelightLensHeightInches) / Math.tan(angleToGoalRadians);
        // System.out.println(distanceFromLimelightToGoalInches);
        return distanceFromLimelightToGoalInches;
    }

    /**
     * @return horizontal offset from crosshair to target as a double. This values range is -27 to
     *     27 degrees.
     */
    public double getHorizontalOffset() {
        double hOffset = tx.getDouble(0);
        return hOffset;
    }

    /**
     * @return vertical offset from crosshair to target as a double. This values range is -20.5 to
     *     20.5 degrees.
     */
    public double getVerticalOffset() {
        double vOffset = ty.getDouble(0);
        return vOffset;
    }

    /** @return from 0% to 100% of the frame/image */
    public double getTargetArea() {
        double targetArea = ta.getDouble(0);
        return targetArea;
    }

    /** @return true if a target is found, false if it is not found */
    public boolean isTargetFound() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable(tableName);
        NetworkTableEntry tv = table.getEntry("tv");
        double target = tv.getDouble(0);

        if (target == 0.0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * sets the current limelight pipeline. Useful for when you want to change the pipeline on
     * different modes, such as teleop and auto
     *
     * @param pipeline - which pipeline you wish to use
     */
    public void setPipeline(int pipeline) {
        NetworkTableInstance.getDefault()
                .getTable(tableName)
                .getEntry("pipeline")
                .setNumber(pipeline);
    }
}
