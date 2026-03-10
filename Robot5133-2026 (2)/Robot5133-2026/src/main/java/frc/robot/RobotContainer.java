package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.commands.AutonomousCommand;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.ClawSubsystem;

public class RobotContainer {
    private final SwerveSubsystem swerve = new SwerveSubsystem();
    private final ShooterSubsystem shooter = new ShooterSubsystem();
    private final ClawSubsystem claw = new ClawSubsystem();  // NUEVO
    private final CommandXboxController controller = new CommandXboxController(0);

    private double speedMultiplier = 0.8;
    private double rotationMultiplier = 1.0;

    private final SendableChooser<Command> autoChooser = new SendableChooser<>();

    public RobotContainer() {
        configureBindings();
        configureAutoCommands();
    }

    private double deadband(double value) {
        return Math.abs(value) < 0.1 ? 0 : value;
    }

    private void configureBindings() {
        // ===== CONTROL DEL CHASIS (SWERVE) =====
        swerve.setDefaultCommand(new RunCommand(() -> {
            double leftY = -deadband(controller.getLeftY());
            double leftX = deadband(controller.getLeftX());
            double rightX = deadband(controller.getRightX());

            double xSpeed = leftY * DriveConstants.kMaxSpeedMetersPerSecond * speedMultiplier;
            double ySpeed = leftX * DriveConstants.kMaxSpeedMetersPerSecond * speedMultiplier;
            double rotSpeed = rightX * DriveConstants.kMaxAngularSpeedRadiansPerSecond * rotationMultiplier;

            swerve.drive(xSpeed, ySpeed, rotSpeed, true);
        }, swerve));

        controller.a().onTrue(new InstantCommand(() -> {
            swerve.calibrateWheels(0);
            System.out.println("⬆️ LLANTAS ALINEADAS AL FRENTE (Y)");
        }, swerve));

        // ===== CONTROL DEL DISPARADOR =====

        // LT (Left Trigger) - DISPARAR (100%)
        controller.leftTrigger().whileTrue(new InstantCommand(() -> {
            shooter.shootHigh();
            System.out.println("🎯 DISPARANDO (LT)");
        }, shooter)).onFalse(new InstantCommand(() -> {
            shooter.stop();
            System.out.println("⏹️ Disparador detenido");
        }, shooter));

        // LB - AGARRAR/SUBIR
        controller.leftBumper().whileTrue(new InstantCommand(() -> {
            claw.grab();
            System.out.println("🔧 AGARRANDO (LB)");
        }, claw)).onFalse(new InstantCommand(() -> {
            claw.stop();
            System.out.println("⏹️ Garras detenidas");
        }, claw));

        // RB - SOLTAR/BAJAR
        controller.rightBumper().whileTrue(new InstantCommand(() -> {
            claw.release();
            System.out.println("🔧 SOLTANDO (RB)");
        }, claw)).onFalse(new InstantCommand(() -> {
            claw.stop();
            System.out.println("⏹️ Garras detenidas");
        }, claw));

        // RT (Right Trigger) - ABSORBER (76%)
        controller.rightTrigger().whileTrue(new InstantCommand(() -> {
            shooter.intake();
            System.out.println("📥 ABSORBIENDO (RT)");
        }, shooter)).onFalse(new InstantCommand(() -> {
            shooter.stop();
            System.out.println("⏹️ Disparador detenido");
        }, shooter));

        // B - EXPULSAR (76%) - Mantenemos por si acaso
        controller.b().whileTrue(new InstantCommand(() -> {
            shooter.reverseIntake();
            System.out.println("📤 EXPULSANDO (B)");
        }, shooter)).onFalse(new InstantCommand(() -> {
            shooter.stop();
            System.out.println("⏹️ Disparador detenido");
        }, shooter));

        /*
         * // ===== CONTROL DEL DISPARADOR =====
         * 
         * // Y - DISPARAR (100%)
         * controller.y().whileTrue(new InstantCommand(() -> {
         * shooter.shootHigh();
         * }, shooter)).onFalse(new InstantCommand(() -> {
         * shooter.stop();
         * }, shooter));
         * 
         * // A - ABSORBER (76%)
         * controller.a().whileTrue(new InstantCommand(() -> {
         * shooter.intake();
         * }, shooter)).onFalse(new InstantCommand(() -> {
         * shooter.stop();
         * }, shooter));
         * 
         * // B - EXPULSAR (76%)
         * controller.b().whileTrue(new InstantCommand(() -> {
         * shooter.reverseIntake();
         * }, shooter)).onFalse(new InstantCommand(() -> {
         * shooter.stop();
         * }, shooter));
         */

        // ===== BOTONES DEL CHASIS =====

        // X - Parar todo (emergencia)
        controller.x().onTrue(new InstantCommand(() -> {
            swerve.stopModules();
            shooter.stop();
            System.out.println("⏹️ PARADA DE EMERGENCIA");
        }, swerve, shooter));

        // START - Aumentar velocidad MOVIMIENTO
        controller.start().onTrue(new InstantCommand(() -> {
            speedMultiplier = Math.min(1.0, speedMultiplier + 0.2);
            System.out.println("⚡ Velocidad movimiento: " + (int) (speedMultiplier * 100) + "%");
        }));

        // BACK - Disminuir velocidad MOVIMIENTO
        controller.back().onTrue(new InstantCommand(() -> {
            speedMultiplier = Math.max(0.4, speedMultiplier - 0.2);
            System.out.println("🐢 Velocidad movimiento: " + (int) (speedMultiplier * 100) + "%");
        }));

        // LB - Aumentar velocidad ROTACIÓN
        controller.leftBumper().onTrue(new InstantCommand(() -> {
            rotationMultiplier = Math.min(1.5, rotationMultiplier + 0.2);
            System.out.println("🔄 Velocidad rotación: " + (int) (rotationMultiplier * 100) + "%");
        }));

        // RB - Disminuir velocidad ROTACIÓN
        controller.rightBumper().onTrue(new InstantCommand(() -> {
            rotationMultiplier = Math.max(0.5, rotationMultiplier - 0.2);
            System.out.println("🐢 Velocidad rotación: " + (int) (rotationMultiplier * 100) + "%");
        }));
    }

    public Command getAutonomousCommand() {
        return autoChooser.getSelected(); // Esto retorna lo seleccionado en SmartDashboard
    }

    // Nuevo método para configurar los autónomos:
    private void configureAutoCommands() {
        // Comando autónomo principal
        Command autoCommand = new AutonomousCommand(swerve, shooter);

        // Opciones en SmartDashboard
        autoChooser.setDefaultOption("Autónomo: Avanzar y Disparar", autoCommand);
        autoChooser.addOption("Solo alinear llantas", new InstantCommand(() -> {
            swerve.calibrateWheels(0);
        }, swerve));
        autoChooser.addOption("Solo disparar 5s", new SequentialCommandGroup(
                new InstantCommand(() -> shooter.shootHigh(), shooter),
                new WaitCommand(5),
                new InstantCommand(() -> shooter.stop(), shooter)));

        SmartDashboard.putData("Autonomous Chooser", autoChooser);
    }

    /*
     * // Modifica getAutonomousCommand() para que retorne lo seleccionado:
     * public Command getAutonomousCommand() {
     * return autoChooser.getSelected();
     * }
     */
}

/*
 * package frc.robot;
 * 
 * import edu.wpi.first.math.geometry.Rotation2d;
 * import edu.wpi.first.math.kinematics.SwerveModuleState;
 * import edu.wpi.first.wpilibj2.command.Command;
 * import edu.wpi.first.wpilibj2.command.InstantCommand;
 * import edu.wpi.first.wpilibj2.command.RunCommand;
 * import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
 * import frc.robot.Constants.DriveConstants;
 * import frc.robot.subsystems.SwerveSubsystem;
 * 
 * public class RobotContainer {
 * private final SwerveSubsystem swerve = new SwerveSubsystem();
 * private final CommandXboxController controller = new
 * CommandXboxController(0);
 * 
 * private double speedMultiplier = 0.8;
 * private double rotationMultiplier = 1.0; // Multiplicador para rotación
 * 
 * public RobotContainer() {
 * configureBindings();
 * }
 * 
 * private double deadband(double value) {
 * return Math.abs(value) < 0.1 ? 0 : value;
 * }
 * 
 * private void configureBindings() {
 * // Control principal
 * // Control principal (movimiento + rotación)
 * // Control principal (movimiento + rotación) - VERSIÓN CORREGIDA
 * swerve.setDefaultCommand(new RunCommand(() -> {
 * // ===== MOVIMIENTO (IGUAL QUE ANTES) =====
 * double leftY = -deadband(controller.getLeftY());
 * double leftX = deadband(controller.getLeftX());
 * 
 * double xSpeed = leftY * DriveConstants.kMaxSpeedMetersPerSecond *
 * speedMultiplier;
 * double ySpeed = leftX * DriveConstants.kMaxSpeedMetersPerSecond *
 * speedMultiplier;
 * 
 * // ===== ROTACIÓN =====
 * double rightX = deadband(controller.getRightX());
 * // Usar rotationMultiplier en lugar de speedMultiplier para la rotación
 * double rotSpeed = -rightX * DriveConstants.kMaxAngularSpeedRadiansPerSecond *
 * rotationMultiplier;
 * 
 * // Debug para ver valores
 * // System.out.println("X: " + xSpeed + " Y: " + ySpeed + " Rot: " +
 * rotSpeed);
 * 
 * swerve.drive(xSpeed, ySpeed, rotSpeed, true);
 * }, swerve));
 * 
 * // Botón A - Alinear llantas
 * controller.a().onTrue(new InstantCommand(() -> {
 * swerve.calibrateWheels(0);
 * System.out.println("🔄 Alineando llantas a 0°");
 * }, swerve));
 * 
 * // Botón B - Parar
 * controller.b().onTrue(new InstantCommand(() -> {
 * swerve.stopModules();
 * System.out.println("⏹️ Parada de emergencia");
 * }, swerve));
 * 
 * // Botón X - Resetear heading
 * controller.x().onTrue(new InstantCommand(() -> {
 * swerve.zeroHeading();
 * System.out.println("🧭 Heading reseteado a 0°");
 * }, swerve));
 * 
 * // Botón Y - Resetear odometría
 * controller.y().onTrue(new InstantCommand(() -> {
 * swerve.resetOdometry(Constants.AutoConstants.START_POSE);
 * System.out.println("📍 Odometría reseteada");
 * }, swerve));
 * 
 * // ===== CONTROLES DE VELOCIDAD =====
 * 
 * // START - Aumentar velocidad de MOVIMIENTO
 * controller.start().onTrue(new InstantCommand(() -> {
 * speedMultiplier = Math.min(1.0, speedMultiplier + 0.2);
 * System.out.println("⚡ Velocidad movimiento: " + (int) (speedMultiplier * 100)
 * + "%");
 * }));
 * 
 * // BACK - Disminuir velocidad de MOVIMIENTO
 * controller.back().onTrue(new InstantCommand(() -> {
 * speedMultiplier = Math.max(0.4, speedMultiplier - 0.2);
 * System.out.println("🐢 Velocidad movimiento: " + (int) (speedMultiplier *
 * 100) + "%");
 * }));
 * 
 * // LB + RB + X - Rotación forzada izquierda
 * controller.leftBumper().and(controller.rightBumper()).and(controller.x()).
 * whileTrue(new InstantCommand(() -> {
 * // Para izquierda, invertimos los ángulos
 * SwerveModuleState[] states = new SwerveModuleState[] {
 * new SwerveModuleState(0.3, Rotation2d.fromDegrees(-45)),
 * new SwerveModuleState(0.3, Rotation2d.fromDegrees(45)),
 * new SwerveModuleState(0.3, Rotation2d.fromDegrees(45)),
 * new SwerveModuleState(0.3, Rotation2d.fromDegrees(-45))
 * };
 * swerve.setModuleStates(states);
 * }, swerve)).onFalse(new InstantCommand(() -> {
 * swerve.stopModules();
 * }, swerve));
 * 
 * // LB - Aumentar velocidad de ROTACIÓN
 * controller.leftBumper().onTrue(new InstantCommand(() -> {
 * rotationMultiplier = Math.min(1.5, rotationMultiplier + 0.2);
 * System.out.println("🔄 Velocidad rotación: " + (int) (rotationMultiplier *
 * 100) + "%");
 * }));
 * 
 * // RB - Disminuir velocidad de ROTACIÓN
 * controller.rightBumper().onTrue(new InstantCommand(() -> {
 * rotationMultiplier = Math.max(0.5, rotationMultiplier - 0.2);
 * System.out.println("🐢 Velocidad rotación: " + (int) (rotationMultiplier *
 * 100) + "%");
 * }));
 * 
 * /*
 * // LB - Aumentar velocidad de ROTACIÓN
 * controller.leftBumper().onTrue(new InstantCommand(() -> {
 * rotationMultiplier = Math.min(1.5, rotationMultiplier + 0.2);
 * System.out.println("🔄 Velocidad rotación: " + (int) (rotationMultiplier *
 * 100) + "%");
 * }));
 * 
 * // RB - Disminuir velocidad de ROTACIÓN
 * controller.rightBumper().onTrue(new InstantCommand(() -> {
 * rotationMultiplier = Math.max(0.5, rotationMultiplier - 0.2);
 * System.out.println("🐢 Velocidad rotación: " + (int) (rotationMultiplier *
 * 100) + "%");
 * }));
 */
/*
 * }
 * 
 * public Command getAutonomousCommand() {
 * return new InstantCommand();
 * }
 * }
 */
