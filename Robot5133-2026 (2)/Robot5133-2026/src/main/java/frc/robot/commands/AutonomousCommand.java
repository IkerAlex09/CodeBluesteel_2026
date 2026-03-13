package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants.AutoConstants;
import frc.robot.subsystems.SwerveSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class AutonomousCommand extends SequentialCommandGroup {
    
    public AutonomousCommand(SwerveSubsystem swerve, ShooterSubsystem shooter) {
        addRequirements(swerve, shooter);
        
        System.out.println("⚙️ CREANDO AUTONOMOUS COMMAND");
        
        addCommands(
        
         //--------------- INICIA "AVANZA SHOOTER" ---------------//
         // 1. Mensaje de inicio
            new InstantCommand(() -> System.out.println("PASO 1: Iniciando autónomo")),
            
            new InstantCommand(() -> {
                System.out.println("PASO 3: Avanzar 1 metro...");
                swerve.drive(AutoConstants.AUTO_SPEED, 0, 0, false);
            }, swerve),
            new WaitCommand(AutoConstants.MOVE_FORWARD_TIME),
            
            new InstantCommand(() -> {
                swerve.stopModules();
                System.out.println("PASO 9: AUTÓNOMO COMPLETADO");
            }, swerve),

            // 5. Disparar por 10 segundos
            new InstantCommand(() -> {
                System.out.println("PASO 5: DISPARANDO...");
                shooter.shootHighL();
            }, shooter),
            new WaitCommand(5),

            // 5. Disparar por 10 segundos
            new InstantCommand(() -> {
                System.out.println("PASO 5: DISPARANDO...");
                shooter.shootHighR();
            }, shooter),
            new WaitCommand(AutoConstants.SHOOT_TIME),

            // 6. Parar disparador
            new InstantCommand(() -> {
                System.out.println("⏹️ PASO 6: Disparador detenido");
                shooter.stop();
            }, shooter),

            // 9. Parar todo
            new InstantCommand(() -> {
                swerve.stopModules();
                System.out.println("PASO 9: AUTÓNOMO COMPLETADO");
            }, swerve)

            //--------------- INICIA "SOLO AVANZA" ---------------//
            /*
            // 1. Mensaje de inicio
            new InstantCommand(() -> System.out.println("🚀 PASO 1: Iniciando autónomo")),
            
            // 2. Alinear llantas al frente
            new InstantCommand(() -> {
                System.out.println("⬆️ PASO 2: Alineando llantas...");
                swerve.calibrateWheels(0);
            }, swerve),
            new WaitCommand(AutoConstants.ALIGN_TIME),
            
            // 3. Avanzar 1 metro
            new InstantCommand(() -> {
                System.out.println("➡️ PASO 3: Avanzando 1 metro...");
                swerve.drive(AutoConstants.AUTO_SPEED, 0, 0, false);
            }, swerve),
            new WaitCommand(AutoConstants.MOVE_FORWARD_TIME),

            // 6. Alinear llantas al final
            new InstantCommand(() -> {
                System.out.println("⬆️ PASO 6: Alineando llantas al final...");
                swerve.calibrateWheels(0);
            }, swerve),
            new WaitCommand(AutoConstants.FINAL_ALIGN_TIME),

            // 7. Parar todo
            new InstantCommand(() -> {
                swerve.stopModules();
                System.out.println("✅ PASO 7: AUTÓNOMO COMPLETADO");
            }, swerve)
            */
         //--------------- TERMINA "SOLO AVANZA" ---------------//
            
        );
        
        System.out.println("✅ AutonomousCommand creado");
    }
}