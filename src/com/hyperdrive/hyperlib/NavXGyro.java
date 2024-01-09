/* Copyright (c) 2024 HyperDrive Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.hyperdrive.hyperlib;

import com.kauailabs.navx.frc.AHRS;

/** This class contains methods for using the NavX-mxp Gyro */
public class NavXGyro {

    /**
     * Determines collision based on the calculation of Jerk If the Jerk (in units of G) exceeds the
     * threshold, a collision is detected.
     *
     * @param navx - the AHRS object (make sure port is set to SPI.Port.kMXP in the constructor)
     * @param threshold - the threshold for the Jerk (in units of G) ie: .5
     * @param lastWorldLinearAccelX - this can be an empty double ie: doule lastWorldLinAccelX;
     * @param lastWorldLinearAccelY - this can be an empty double ie: doule lastWorldLinAccelY;
     * @return - true if a collision is detected, false if not
     */
    public boolean detectCollision(
            AHRS navx,
            double threshold,
            double lastWorldLinearAccelX,
            double lastWorldLinearAccelY) {
        boolean collisionDetected = false;
        double currentWorldLinearAccelX = navx.getWorldLinearAccelX();
        double currentWorldLinearAccelY = navx.getWorldLinearAccelY();
        double currentJerkX = currentWorldLinearAccelX - lastWorldLinearAccelX;
        double currentJerkY = currentWorldLinearAccelY - lastWorldLinearAccelY;
        lastWorldLinearAccelX = currentWorldLinearAccelX;
        lastWorldLinearAccelY = currentWorldLinearAccelY;
        if ((Math.abs(currentJerkX) > threshold) || (Math.abs(currentJerkY) > threshold)) {
            collisionDetected = true;
        }
        return collisionDetected;
    }

    /**
     * Returns the robot's position in a 2D vector
     *
     * @param navX - the AHRS object (make sure port is set to SPI.Port.kMXP in the constructor)
     * @return - a 2D vector containing the robot's position
     */
    public Vector2 getRobotPosition(AHRS navX) {
        // convert float coord into doubles implicitly
        float disx = navX.getDisplacementX();
        float disy = navX.getDisplacementY();
        double x = disx;
        double y = disy;
        return new Vector2(x, y);
    }
}
