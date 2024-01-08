/* Copyright (c) 2024 HyperDrive Robotics. All rights reserved. */
/* This work is licensed under the terms of the MIT license */
/* found in the root directory of this project. */

package com.hyperdrive.hyperlib;

import edu.wpi.first.apriltag.AprilTagDetection;
import edu.wpi.first.apriltag.AprilTagDetector;
import edu.wpi.first.apriltag.AprilTagDetector.Config;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.wpilibj.Timer;

import java.util.HashSet;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/** Provides an easy interface to detect apriltags in one function call. */
public class AprilTagFinder {

    // by running vision processing in a separate thread, we can avoid blocking the main thread, and
    // boost performance over single-threaded
    Thread visionThread;
    private final int cameraID;
    private final int width;
    private final int height;
    private final int fps;
    private final String family;

    private volatile int tagID;
    private volatile int detectionsPerSecond;

    /**
     * Creates a new AprilTagFinder
     *
     * @param cameraID - the ID of the camera to use, for example 0 for the first camera
     * @param width - the width of the camera feed in pixels (4:3 aspect ratio is reccomended)
     * @param height - the height of the camera feed in pixels (4:3 aspect ratio is reccomended)
     * @param fps - the framerate of the camera feed (30 is reccomended)
     * @param family - the family of AprilTags to detect, for example "tag36h11"
     */
    public AprilTagFinder(int cameraID, int width, int height, int fps, String family) {
        this.cameraID = cameraID;
        this.width = width;
        this.height = height;
        this.fps = fps;
        this.family = family;
    }

    public void startDetection() {
        visionThread =
                new Thread(
                        () -> {
                            UsbCamera camera = CameraServer.startAutomaticCapture(cameraID);
                            camera.setResolution(width, height);
                            camera.setFPS(fps);
                            CvSink sink = CameraServer.getVideo();
                            CvSource outputStream =
                                    CameraServer.putVideo("AprilTagDetection", width, height);

                            // Mat's are memory expensive, its better to re-use them instead of
                            // allocating new ones
                            // but for now we will be using two.
                            Mat mat = new Mat();
                            Mat grayMat = new Mat();

                            // Points needed to draw square around detected AprilTag
                            Point pt0 = new Point();
                            Point pt1 = new Point();
                            Point pt2 = new Point();
                            Point pt3 = new Point();
                            Point center = new Point();

                            Scalar red =
                                    new Scalar(
                                            0, 0,
                                            255); // instead of following the RGB color scheme,
                            // OpenCV uses BGR
                            Scalar green = new Scalar(0, 255, 0);

                            AprilTagDetector aprilTagDetector = new AprilTagDetector();
                            Config config = aprilTagDetector.getConfig();
                            config.quadSigma =
                                    0.8f; // quadSigma affects the blurring of the image before
                            // detection,
                            // higher = more blurring (smooth edges, better for lower contrast, less
                            // sensitive to noise),
                            // lower = less blurring (sharp corners or edges, better for high
                            // contrast, more sensitive to noise).

                            aprilTagDetector.setConfig(config);

                            var quadThreshParams = aprilTagDetector.getQuadThresholdParameters();
                            quadThreshParams.minClusterPixels = 400;
                            quadThreshParams.criticalAngle *= 5; // default is 10
                            quadThreshParams.maxLineFitMSE *= 1.5;
                            aprilTagDetector.setQuadThresholdParameters(quadThreshParams);

                            // the AprilTagDetector only detects one AprilTag family at a time, to
                            // detect multiple families, use multiple AprilTagDetectors
                            // of course, this comes at an expensive cost to performance.
                            aprilTagDetector.addFamily(family);

                            Timer timer = new Timer();
                            timer.start();
                            int count = 0;

                            // This can never be true, for it to be true the robot must be off, or
                            // the program must be stopped/killed.
                            while (!Thread.interrupted()) {
                                if (sink.grabFrame(mat) == 0) {
                                    outputStream.notifyError(sink.getError());
                                    continue;
                                }

                                // convert mat to grayscale
                                Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY);

                                AprilTagDetection[] results = aprilTagDetector.detect(grayMat);

                                // Using HashSet to avoid duplicate tags, and improve performance
                                var set = new HashSet<>();

                                for (var result : results) {
                                    count += 1;
                                    pt0.x = result.getCornerX(0);
                                    pt1.x = result.getCornerX(1);
                                    pt2.x = result.getCornerX(2);
                                    pt3.x = result.getCornerX(3);

                                    pt0.y = result.getCornerY(0);
                                    pt1.y = result.getCornerY(1);
                                    pt2.y = result.getCornerY(2);
                                    pt3.y = result.getCornerY(3);

                                    center.x = result.getCenterX();
                                    center.y = result.getCenterY();

                                    set.add(result.getId());
                                    tagID = result.getId();

                                    // draw square around detected AprilTag
                                    Imgproc.line(mat, pt0, pt1, red, 5);
                                    Imgproc.line(mat, pt1, pt2, red, 5);
                                    Imgproc.line(mat, pt2, pt3, red, 5);
                                    Imgproc.line(mat, pt3, pt0, red, 5);

                                    Imgproc.circle(mat, center, 4, green);
                                    // display id (number) of the tag
                                    Imgproc.putText(
                                            mat,
                                            String.valueOf(result.getId()),
                                            pt2,
                                            Imgproc.FONT_HERSHEY_COMPLEX,
                                            2,
                                            green,
                                            7);
                                }

                                for (var id : set) {
                                    System.out.println("Tag: " + String.valueOf(id));
                                }

                                if (timer.advanceIfElapsed(1.0)) {
                                    detectionsPerSecond = count;
                                    System.out.println(
                                            "detections per second: " + String.valueOf(count));
                                    count = 0;
                                }

                                outputStream.putFrame(mat);
                            }
                            // if you do not close the detector, it will cause a memory leak
                            aprilTagDetector.close();
                        });
        visionThread.setDaemon(true);
        visionThread.start();
    }

    /**
     * Returns the ID of the detected AprilTag
     *
     * @return
     */
    public int getTagID() {
        return tagID;
    }

    /**
     * Returns the number of detections per second
     *
     * @return
     */
    public int getDetectionsPerSecond() {
        return detectionsPerSecond;
    }
}
