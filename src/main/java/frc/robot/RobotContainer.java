// In Java We Trust

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.commands.SwerveCommand;
import frc.robot.subsystems.AutonManager;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.vision.AprilTagVision;
import frc.robot.subsystems.vision.AprilTagVisionIONorthstar;

public class RobotContainer {

    private SwerveSubsystem robotSwerve;
    private AutonManager autonManager;

    private CommandXboxController controller;
    
    private Trigger zeroGyro;
    private Trigger robotCentric;

    private AprilTagVision aprilTagVision;


    public RobotContainer() {
        robotSwerve = new SwerveSubsystem();
        autonManager = new AutonManager(robotSwerve);

        controller = new CommandXboxController(0);

        zeroGyro = controller.leftBumper();
        robotCentric = controller.rightBumper();

        robotSwerve.setDefaultCommand(new SwerveCommand(
            robotSwerve, 
            () -> -controller.getLeftY(), 
            () -> -controller.getLeftX(), 
            () -> controller.getRightX(), 
            () -> robotCentric.getAsBoolean()
        ));

        aprilTagVision = new AprilTagVision(new AprilTagVisionIONorthstar("northstar_0"));


        aprilTagVision.setDataInterfaces(robotSwerve::addVisionData, robotSwerve::getPose);

        configureBindings();
    }

    private void configureBindings() {
        zeroGyro.onTrue(new InstantCommand( () -> { 
            robotSwerve.zeroGyro(); 
        }))
        .onFalse(new InstantCommand());

        controller.b().onTrue(new InstantCommand( () -> {
            robotSwerve.resetModules();
        }))
        .onFalse(new InstantCommand());

       
    }

    public Command getAutonomousCommand() {
        return autonManager.followPathCommand("testPath");
    }
}
