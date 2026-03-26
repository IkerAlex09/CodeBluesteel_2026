package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;

public class Robot extends TimedRobot {
    private Command autonomousCommand;
    private RobotContainer robotContainer;

    @Override
    public void robotInit() {
        System.out.println("\n=== ROBOT 5133 INICIALIZANDO ===");
        robotContainer = new RobotContainer();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void teleopInit() {
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }

    @Override
    public void autonomousInit() {
        System.out.println("=== INICIANDO AUTÓNOMO ===");
        autonomousCommand = robotContainer.getAutonomousCommand();

        // DEBUG: Mostrar qué comando se seleccionó
        if (autonomousCommand != null) {
            System.out.println("✅ Comando seleccionado: " + autonomousCommand.getClass().getSimpleName());
            System.out.println("¿Es SequentialCommandGroup? " + (autonomousCommand instanceof SequentialCommandGroup));

            autonomousCommand.schedule();
            System.out.println("✅ Comando programado");
        } else {
            System.out.println("❌ No hay comando seleccionado (null)");
        }
    }

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
    }
}