/* Copyright (c) 2023 HyperDrive Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.hyperdrive.hyperlib;

public class Vector3 {
    public final double x;
    public final double y;
    public final double z;

    /**
     * @param x - x axis of the vector
     * @param y - y axis of the vector
     */
    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Vector3 consts
    public static final Vector3 origin = new Vector3(0, 0, 0);
    public static final Vector3 up = new Vector3(0, 1, 0);
    public static final Vector3 down = new Vector3(0, -1, 0);
    public static final Vector3 left = new Vector3(-1, 0, 0);
    public static final Vector3 right = new Vector3(1, 0, 0);

    /**
     * adds two Vector3's
     *
     * @param origin - the original vector
     * @param other - the vector that will be added to the origin vector
     * @param ignoreZAxis - if true, the z axis will remain the same
     * @return the sum of both vectors
     */
    public static Vector3 add(Vector3 origin, Vector3 other, boolean ignoreZAxis) {
        double x = origin.x + other.x;
        double y = origin.y + other.y;
        double z;
        if (!ignoreZAxis) {
            z = origin.z + other.z;
        } else {
            z = origin.z;
        }
        return new Vector3(x, y, z);
    }

    /**
     * subtracts a Vector3 from another Vector3
     *
     * @param origin - the original vector
     * @param other - the vector that will be subracted from the origin vector
     * @return the difference of both vectors
     */
    public static Vector3 sub(Vector3 origin, Vector3 other) {
        double x = origin.x - other.x;
        double y = origin.y - other.y;
        double z = origin.z - other.z;
        return new Vector3(x, y, z);
    }

    /**
     * Checks wether two Vector3's are equal to each other
     *
     * @param origin - the original vector
     * @param other - the other vector
     * @return - true, if both Vectors are equal to each other; false, if not.
     */
    public static boolean eq(Vector3 origin, Vector3 other) {
        if (origin.x == other.x && origin.y == other.y && origin.z == other.z) {
            return true;
        }
        return false;
    }

    /**
     * 'flips' or inverts a vector by multiplying the x and y by -1
     *
     * @param origin - the original vector
     * @param ignoreZAxis - if true, the Z axis will be ignored
     * @return - the 'flipped'/inverted vector
     */
    public static Vector3 flip(Vector3 origin, boolean ignoreZAxis) {
        double x = origin.x * -1;
        double y = origin.y * -1;
        double z;
        if (!ignoreZAxis) {
            z = origin.z * -1;
        } else {
            z = origin.z;
        }
        return new Vector3(x, y, z);
    }

    /**
     * Rotates a Vector based on a given angle (in degrees)
     *
     * @param vector - the vector to rotate
     * @param angleDegrees - the angle at which the vector will be rotated.
     * @return - the rotated vector
     */
    public static Vector3 rotate2d(Vector3 vector, double angleDegrees) {
        // Convert angle from degrees to radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Compute the sine and cosine of the angle
        double cosAngle = Math.cos(angleRadians);
        double sinAngle = Math.sin(angleRadians);

        // Apply rotation matrix to the vector
        double rotatedX = vector.x * cosAngle - vector.y * sinAngle;
        double rotatedY = vector.x * sinAngle + vector.y * cosAngle;
        double rotatedZ = vector.z; // No rotation in the Z-axis for 2D rotation

        return new Vector3(rotatedX, rotatedY, rotatedZ);
    }

    /**
     * scales a Vector3 per the scale parameter
     *
     * @param origin - the original vector
     * @param scaler - the scale that the vector will be multiplied by
     * @param ignoreZAxis - if true, the Z-axis will remain the same
     * @return - the scaled vector
     */
    public static Vector3 scale(Vector3 origin, double scaler, boolean ignoreZAxis) {
        double x = origin.x * scaler;
        double y = origin.y * scaler;
        double z;
        if (!ignoreZAxis) {
            z = origin.z * scaler;
        } else {
            z = origin.z;
        }
        return new Vector3(x, y, z);
    }

    /**
     * mostly the same as scale, but Vector3's can be scaled on the x, and y axis individually
     *
     * @param origin - the original vector
     * @param scalerX - the scale that the X of the Vector will be multiplied by
     * @param scalerY - the scale that the Y of the Vector will be multiplied by
     * @param scalerZ - the cale that the Z of the Vector will be multiplied by
     * @return - the scaled vector
     */
    public static Vector3 scaleSpecific(
            Vector3 origin, double scalerX, double scalerY, double scalerZ) {
        double x = origin.x * scalerX;
        double y = origin.y * scalerY;
        double z = origin.z * scalerZ;
        return new Vector3(x, y, z);
    }

    /**
     * calculates the midpoint between two points using the midpoint formula: ((x1 + x2)/2, (y1 +
     * y2)/2)
     *
     * @param origin - the starting point
     * @param end - the end point
     * @return - the middle between the origin and end vector
     */
    public static Vector3 getMidpoint2d(Vector3 origin, Vector3 end) {
        double x = (origin.x + end.x) / 2;
        double y = (origin.y + end.y) / 2;
        double z = origin.z;
        return new Vector3(x, y, z);
    }
}
