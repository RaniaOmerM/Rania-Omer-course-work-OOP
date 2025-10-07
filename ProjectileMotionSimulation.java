/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.projectilemotionsimulation;

import java.util.Scanner;
/**
 *
 * @author Magbo
 */
public class ProjectileMotionSimulation {

public static void main(String[] args) {
    Scanner input = new Scanner(System.in);

    // Gravitational acceleration constant (m/s^2)
    final double g = 9.8;

    System.out.println("=== Projectile Motion Simulation Program ===");
    System.out.println("This program calculates the time of flight, maximum height, and horizontal range.\n");

    // Input initial velocity
    System.out.print("Enter initial velocity (m/s): ");
    double velocity = input.nextDouble();

    // Input launch angle
    System.out.print("Enter launch angle (in degrees): ");
    double angleDegrees = input.nextDouble();

    // Convert angle from degrees to radians (Java trigonometric functions use radians)
    double angleRadians = Math.toRadians(angleDegrees);

    // Physics equations
    double timeOfFlight = (2 * velocity * Math.sin(angleRadians)) / g;
    double maxHeight = (Math.pow(velocity * Math.sin(angleRadians), 2)) / (2 * g);
    double range = (Math.pow(velocity, 2) * Math.sin(2 * angleRadians)) / g;

    // Display results
    System.out.println("\n===== Results =====");
    System.out.printf("Time of flight (seconds): %.2f\n", timeOfFlight);
    System.out.printf("Maximum height (meters): %.2f\n", maxHeight);
    System.out.printf("Horizontal range (meters): %.2f\n", range);
    System.out.println("===================");

    // Close the Scanner
    input.close();
}
}
