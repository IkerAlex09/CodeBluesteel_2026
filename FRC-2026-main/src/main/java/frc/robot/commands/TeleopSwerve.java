package frc.robot.commands;

import frc.robot.Constants;
import frc.robot.subsystems.Swerve;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;
import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;


public class TeleopSwerve extends Command {    
    private Swerve s_Swerve;    
    private XboxController s_driver;
    private DoubleSupplier translationSup;
    private DoubleSupplier strafeSup;
    private DoubleSupplier rotationSup;
    private BooleanSupplier robotCentricSup;
    private double maxSpeed;

    public TeleopSwerve(Swerve s_Swerve, XboxController s_driver ,DoubleSupplier translationSup, DoubleSupplier strafeSup, DoubleSupplier rotationSup, BooleanSupplier robotCentricSup) {
        this.s_Swerve = s_Swerve;
        this.s_driver = s_driver;
        addRequirements(s_Swerve);

        this.translationSup = translationSup;
        this.strafeSup = strafeSup;
        this.rotationSup = rotationSup;
        this.robotCentricSup = robotCentricSup;
    }

    @Override
public void execute() {

    double translationVal = MathUtil.applyDeadband(
        translationSup.getAsDouble(),
        Constants.stickDeadband
    );

    double strafeVal = MathUtil.applyDeadband(
        strafeSup.getAsDouble(),
        Constants.stickDeadband
    );

    double rotationVal = MathUtil.applyDeadband(
        rotationSup.getAsDouble(),
        Constants.stickDeadband
    );

    s_Swerve.drive(
        new Translation2d(translationVal, strafeVal)
            .times(Constants.kMaxSpeedMetersPerSecond),
        rotationVal * Constants.kMaxAngularSpeed,
        !robotCentricSup.getAsBoolean(),
        true
    );
}
}