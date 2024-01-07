/* Copyright (c) 2024 HyperDrive Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.hyperdrive.hyperlib;

public class Vector2 {

    public final double x;
    public final double y;

    /**
     * @param x - x axis of the vector
     * @param y - y axis of the vector
     */
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    // Vector2 consts
    public static final Vector2 origin = new Vector2(0, 0);
    public static final Vector2 up = new Vector2(0, 1);
    public static final Vector2 down = new Vector2(0, -1);
    public static final Vector2 left = new Vector2(-1, 0);
    public static final Vector2 right = new Vector2(1, 0);

    /**
     * adds two Vector2's
     *
     * @param origin - the original vector
     * @param other - the vector that will be added to the origin vector
     * @return the sum of both vectors
     */
    public static Vector2 add(Vector2 origin, Vector2 other) {
        double x = origin.x + other.x;
        double y = origin.y + other.y;
        return new Vector2(x, y);
    }

    /**
     * subtracts a Vector2 from another Vector2
     *
     * @param origin - the original vector
     * @param other - the vector that will be subracted from the origin vector
     * @return the difference of both vectors
     */
    public static Vector2 sub(Vector2 origin, Vector2 other) {
        double x = origin.x - other.x;
        double y = origin.y - other.y;
        return new Vector2(x, y);
    }

    /**
     * Checks wether two Vector2's are equal to each other
     *
     * @param origin - the original vector
     * @param other - the other vector
     * @return - true, if both Vectors are equal to each other; false, if not.
     */
    public static boolean eq(Vector2 origin, Vector2 other) {
        if (origin.x == other.x && origin.y == other.y) {
            return true;
        }
        return false;
    }

    /**
     * 'flips' or inverts a vector by multiplying the x and y by -1
     *
     * @param origin - the original vector
     * @return - the 'flipped'/inverted vector
     */
    public static Vector2 flip(Vector2 origin) {
        double x = origin.x * -1;
        double y = origin.y * -1;
        return new Vector2(x, y);
    }

    /**
     * Rotates a Vector based on a given angle (in degrees)
     *
     * @param vector - the vector to rotate
     * @param angleDegrees - the angle at which the vector will be rotated.
     * @return - the rotated vector
     */
    public static Vector2 rotate(Vector2 vector, double angleDegrees) {
        // Convert angle from degrees to radians
        double angleRadians = Math.toRadians(angleDegrees);

        // Compute the sine and cosine of the angle
        double cosAngle = Math.cos(angleRadians);
        double sinAngle = Math.sin(angleRadians);

        // Apply rotation matrix to the vector
        double rotatedX = vector.x * cosAngle - vector.y * sinAngle;
        double rotatedY = vector.x * sinAngle + vector.y * cosAngle;

        return new Vector2(rotatedX, rotatedY);
    }

    /**
     * scales a Vector2 per the scale parameter
     *
     * @param origin - the original vector
     * @param scaler - the scale that the vector will be multiplied by
     * @return - the scaled vector
     */
    public static Vector2 scale(Vector2 origin, double scaler) {
        double x = origin.x * scaler;
        double y = origin.y * scaler;
        return new Vector2(x, y);
    }

    /**
     * mostly the same as scale, but Vector2's can be scaled on the x, and y axis individually
     *
     * @param origin - the original vector
     * @param scalerX - the scale that the X of the Vector will be multiplied by
     * @param scalerY - the scale that the Y of the Vector will be multiplied by
     * @return - the scaled vector
     */
    public static Vector2 scaleSpecific(Vector2 origin, double scalerX, double scalerY) {
        double x = origin.x * scalerX;
        double y = origin.y * scalerY;
        return new Vector2(x, y);
    }

    /**
     * calculates the midpoint between two points using the midpoint formula: ((x1 + x2)/2, (y1 +
     * y2)/2)
     *
     * @param origin - the starting point
     * @param end - the end point
     * @return - the middle between the origin and end vector
     */
    public static Vector2 getMidpoint(Vector2 origin, Vector2 end) {
        double x = (origin.x + end.x) / 2;
        double y = (origin.y + end.y) / 2;
        return new Vector2(x, y);
    }
}
