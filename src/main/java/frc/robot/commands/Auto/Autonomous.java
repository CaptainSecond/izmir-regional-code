// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Auto;

import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Auto.AutoBall.AutoVisionBall;
import frc.robot.subsystems.ClimbSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.TunnelSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.LEDSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.TriggerSubsystem;
import frc.robot.subsystems.TurretSubsystem;
import frc.robot.subsystems.VisionSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class Autonomous extends SequentialCommandGroup {
  /** Creates a new Autonomous. */
  public Autonomous(
      DriveSubsystem drive,
      TunnelSubsystem tunnel,
      ShooterSubsystem shooter,
      TurretSubsystem turret,
      VisionSubsystem vision,
      ClimbSubsystem climb,
      IntakeSubsystem intake,
      LEDSubsystem led,
      TriggerSubsystem trigger) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
        new AutoVisionBall(turret, vision, shooter, trigger, tunnel, 0.5, 1),
        new ParallelCommandGroup(
            new RunCommand(() -> intake.runIntake(1), intake),
            new RunCommand(() -> drive.tankDrive(-0.5, -0.5), drive)) {
          @Override
          public void end(boolean interrupted) {
            intake.runIntake(0);
            drive.tankDrive(0, 0);
          }
        }.withTimeout(2),
        new ParallelCommandGroup(
            new RunCommand(() -> drive.tankDrive(0.5, 0.5), drive)) {
          @Override
          public void end(boolean interrupted) {
            drive.tankDrive(0, 0);
          }
        }.withTimeout(2),
        new AutoVisionBall(turret, vision, shooter, trigger, tunnel, 0.5, 1));
  }
}
