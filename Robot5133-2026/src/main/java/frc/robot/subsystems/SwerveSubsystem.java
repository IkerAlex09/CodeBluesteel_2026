package frc.robot.subsystems;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DriveConstants;

public class SwerveSubsystem extends SubsystemBase {
    private final SwerveModule frontLeft;
    private final SwerveModule frontRight;
    private final SwerveModule backLeft;
    private final SwerveModule backRight;

    private final AHRS gyro;
    private final SwerveDriveOdometry odometer;

    public SwerveSubsystem() {
        System.out.println("=== INICIALIZANDO SWERVE SUBSYSTEM ===");

        frontLeft = new SwerveModule(
                DriveConstants.kFrontLeftDriveMotorPort,
                DriveConstants.kFrontLeftTurningMotorPort,
                DriveConstants.kFrontLeftDriveEncoderReversed,
                DriveConstants.kFrontLeftTurningEncoderReversed,
                DriveConstants.kFrontLeftDriveAbsoluteEncoderPort,
                DriveConstants.kFrontLeftDriveAbsoluteEncoderOffsetRad,
                DriveConstants.kFrontLeftDriveAbsoluteEncoderReversed);

        frontRight = new SwerveModule(
                DriveConstants.kFrontRightDriveMotorPort,
                DriveConstants.kFrontRightTurningMotorPort,
                DriveConstants.kFrontRightDriveEncoderReversed,
                DriveConstants.kFrontRightTurningEncoderReversed,
                DriveConstants.kFrontRightDriveAbsoluteEncoderPort,
                DriveConstants.kFrontRightDriveAbsoluteEncoderOffsetRad,
                DriveConstants.kFrontRightDriveAbsoluteEncoderReversed);

        backLeft = new SwerveModule(
                DriveConstants.kBackLeftDriveMotorPort,
                DriveConstants.kBackLeftTurningMotorPort,
                DriveConstants.kBackLeftDriveEncoderReversed,
                DriveConstants.kBackLeftTurningEncoderReversed,
                DriveConstants.kBackLeftDriveAbsoluteEncoderPort,
                DriveConstants.kBackLeftDriveAbsoluteEncoderOffsetRad,
                DriveConstants.kBackLeftDriveAbsoluteEncoderReversed);

        backRight = new SwerveModule(
                DriveConstants.kBackRightDriveMotorPort,
                DriveConstants.kBackRightTurningMotorPort,
                DriveConstants.kBackRightDriveEncoderReversed,
                DriveConstants.kBackRightTurningEncoderReversed,
                DriveConstants.kBackRightDriveAbsoluteEncoderPort,
                DriveConstants.kBackRightDriveAbsoluteEncoderOffsetRad,
                DriveConstants.kBackRightDriveAbsoluteEncoderReversed);

        gyro = new AHRS(SPI.Port.kMXP);

        odometer = new SwerveDriveOdometry(
                DriveConstants.kDriveKinematics,
                getRotation2d(),
                getModulePositions(),
                new Pose2d());

        new Thread(() -> {
            try {
                Thread.sleep(1000);
                zeroHeading();
                System.out.println("✅ Heading reseteado automáticamente");
            } catch (Exception e) {
            }
        }).start();

        System.out.println("✅ SwerveSubsystem inicializado");
    }

    public void zeroHeading() {
        gyro.reset();
    }

    public double getHeading() {
        return Math.IEEEremainder(gyro.getAngle(), 360);
    }

    public Rotation2d getRotation2d() {
        // Si el robot gira al revés, cambia el signo
        return Rotation2d.fromDegrees(-getHeading()); // Prueba con y sin negativo

        // O prueba con offset de 90°:
        // return Rotation2d.fromDegrees(getHeading() + 90);
    }

    public Pose2d getPose() {
        return odometer.getPoseMeters();
    }

    public void resetOdometry(Pose2d pose) {
        odometer.resetPosition(
                getRotation2d(),
                getModulePositions(),
                pose);
        System.out.println("📍 Odometría reseteada a: " + pose.getTranslation().toString());
    }

    public void calibrateWheels(double angle) {
    System.out.println("🔧 INICIANDO ALINEACIÓN AUTOMÁTICA...");
    System.out.println("  Ángulos actuales:");
    System.out.println("    FL: " + frontLeft.getTurningPosition() + " rad");
    System.out.println("    FR: " + frontRight.getTurningPosition() + " rad");
    System.out.println("    BL: " + backLeft.getTurningPosition() + " rad");
    System.out.println("    BR: " + backRight.getTurningPosition() + " rad");
    
    SwerveModuleState state = new SwerveModuleState(0, Rotation2d.fromDegrees(angle));
    frontLeft.setDesiredState(state);
    frontRight.setDesiredState(state);
    backLeft.setDesiredState(state);
    backRight.setDesiredState(state);
    
    System.out.println("✅ LLANTAS ALINEADAS AL FRENTE");
}

    /*
    public void calibrateWheels(double angle) {
        System.out.println("🔧 Calibrando llantas a " + angle + "°");
        SwerveModuleState state = new SwerveModuleState(0, Rotation2d.fromDegrees(angle));
        frontLeft.setDesiredState(state);
        frontRight.setDesiredState(state);
        backLeft.setDesiredState(state);
        backRight.setDesiredState(state);
    }*/

    public void stopModules() {
        frontLeft.stop();
        frontRight.stop();
        backLeft.stop();
        backRight.stop();
    }
/*
    public void testRotationForced(double power) {
        System.out.println("=== PRUEBA DE ROTACIÓN FORZADA ===");

        // Configurar ángulos para rotación derecha
        Rotation2d flAngle = Rotation2d.fromDegrees(45);
        Rotation2d frAngle = Rotation2d.fromDegrees(-45);
        Rotation2d blAngle = Rotation2d.fromDegrees(-45);
        Rotation2d brAngle = Rotation2d.fromDegrees(45);

        // Crear estados con la potencia dada
        SwerveModuleState[] states = new SwerveModuleState[] {
                new SwerveModuleState(power, flAngle),
                new SwerveModuleState(power, frAngle),
                new SwerveModuleState(power, blAngle),
                new SwerveModuleState(power, brAngle)
        };

        // Enviar directamente a los módulos
        frontLeft.setDesiredState(states[0]);
        frontRight.setDesiredState(states[1]);
        backLeft.setDesiredState(states[2]);
        backRight.setDesiredState(states[3]);

        System.out.println("FL: " + flAngle.getDegrees() + "°");
        System.out.println("FR: " + frAngle.getDegrees() + "°");
        System.out.println("BL: " + blAngle.getDegrees() + "°");
        System.out.println("BR: " + brAngle.getDegrees() + "°");
    }*/

    public void drive(double xSpeed, double ySpeed, double rotSpeed, boolean fieldRelative) {
    SwerveModuleState[] states = DriveConstants.kDriveKinematics.toSwerveModuleStates(
        fieldRelative
            ? edu.wpi.first.math.kinematics.ChassisSpeeds.fromFieldRelativeSpeeds(
                xSpeed, ySpeed, rotSpeed, getRotation2d())
            : new edu.wpi.first.math.kinematics.ChassisSpeeds(xSpeed, ySpeed, rotSpeed)
    );
    
    setModuleStates(states);
    
    // ===== NUEVO DEBUG PARA VER ÁNGULOS =====
    // Solo mostrar cuando hay rotación significativa
    if (Math.abs(rotSpeed) > 0.05) {
        System.out.println("\n=== INTENTANDO ROTAR ===");
        System.out.println("RotSpeed: " + rotSpeed);
        System.out.println("Ángulos calculados por cinemática:");
        System.out.println("  FL: " + states[0].angle.getDegrees() + "°");
        System.out.println("  FR: " + states[1].angle.getDegrees() + "°");
        System.out.println("  BL: " + states[2].angle.getDegrees() + "°");
        System.out.println("  BR: " + states[3].angle.getDegrees() + "°");
        
        // También mostrar velocidades
        System.out.println("Velocidades:");
        System.out.println("  FL: " + states[0].speedMetersPerSecond);
        System.out.println("  FR: " + states[1].speedMetersPerSecond);
        System.out.println("  BL: " + states[2].speedMetersPerSecond);
        System.out.println("  BR: " + states[3].speedMetersPerSecond);
    }
}

    /*
     * public void drive(double xSpeed, double ySpeed, double rotSpeed, boolean
     * fieldRelative) {
     * SwerveModuleState[] states =
     * DriveConstants.kDriveKinematics.toSwerveModuleStates(
     * fieldRelative
     * ? edu.wpi.first.math.kinematics.ChassisSpeeds.fromFieldRelativeSpeeds(
     * xSpeed, ySpeed, rotSpeed, getRotation2d())
     * : new edu.wpi.first.math.kinematics.ChassisSpeeds(xSpeed, ySpeed, rotSpeed));
     * 
     * setModuleStates(states);
     * }
     */

    public void setModuleStates(SwerveModuleState[] desiredStates) {
        SwerveDriveKinematics.desaturateWheelSpeeds(
                desiredStates,
                DriveConstants.kPhysicalMaxSpeedMetersPerSecond);

        frontLeft.setDesiredState(desiredStates[0]);
        frontRight.setDesiredState(desiredStates[1]);
        backLeft.setDesiredState(desiredStates[2]);
        backRight.setDesiredState(desiredStates[3]);
    }

    public SwerveModulePosition[] getModulePositions() {
        return new SwerveModulePosition[] {
                frontLeft.getPosition(),
                frontRight.getPosition(),
                backLeft.getPosition(),
                backRight.getPosition()
        };
    }

    public SwerveModuleState[] getModuleStates() {
        return new SwerveModuleState[] {
                frontLeft.getState(),
                frontRight.getState(),
                backLeft.getState(),
                backRight.getState()
        };
    }

    @Override
    public void periodic() {
        odometer.update(getRotation2d(), getModulePositions());

        SmartDashboard.putNumber("Robot Heading", getHeading());
        SmartDashboard.putNumber("Robot Rotation", getRotation2d().getDegrees());
        SmartDashboard.putString("Robot Location", getPose().getTranslation().toString());
        SmartDashboard.putNumber("Robot X", getPose().getX());
        SmartDashboard.putNumber("Robot Y", getPose().getY());
    }
}
/*
 * package frc.robot.subsystems;
 * 
 * import edu.wpi.first.wpilibj2.command.SubsystemBase;
 * import edu.wpi.first.math.geometry.Pose2d;
 * import edu.wpi.first.math.geometry.Rotation2d;
 * import edu.wpi.first.math.geometry.Translation2d;
 * import edu.wpi.first.math.kinematics.ChassisSpeeds;
 * import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
 * import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
 * import edu.wpi.first.math.kinematics.SwerveModulePosition;
 * import edu.wpi.first.math.kinematics.SwerveModuleState;
 * import edu.wpi.first.wpilibj.SPI;
 * import edu.wpi.first.wpilibj.Timer;
 * import edu.wpi.first.wpilibj.smartdashboard.Field2d;
 * import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
 * import com.kauailabs.navx.frc.AHRS;
 * import frc.robot.Constants.DriveConstants;
 * 
 * public class SwerveSubsystem extends SubsystemBase {
 * private final SwerveModule[] swerveModules;
 * private final AHRS navx;
 * private final SwerveDriveOdometry odometry;
 * private final Field2d field = new Field2d();
 * 
 * public SwerveSubsystem() {
 * System.out.println("=== INICIALIZANDO SWERVE ===");
 * 
 * navx = new AHRS(SPI.Port.kMXP);
 * Timer.delay(1.0);
 * 
 * swerveModules = new SwerveModule[] {
 * new SwerveModule(0,
 * DriveConstants.kFrontLeftDriveMotorPort,
 * DriveConstants.kFrontLeftTurningMotorPort,
 * DriveConstants.kFrontLeftDriveAbsoluteEncoderPort,
 * DriveConstants.kFrontLeftDriveAbsoluteEncoderOffsetRad,
 * DriveConstants.kFrontLeftDriveEncoderReversed,
 * DriveConstants.kFrontLeftTurningEncoderReversed),
 * new SwerveModule(1,
 * DriveConstants.kFrontRightDriveMotorPort,
 * DriveConstants.kFrontRightTurningMotorPort,
 * DriveConstants.kFrontRightDriveAbsoluteEncoderPort,
 * DriveConstants.kFrontRightDriveAbsoluteEncoderOffsetRad,
 * DriveConstants.kFrontRightDriveEncoderReversed,
 * DriveConstants.kFrontRightTurningEncoderReversed),
 * new SwerveModule(2,
 * DriveConstants.kBackLeftDriveMotorPort,
 * DriveConstants.kBackLeftTurningMotorPort,
 * DriveConstants.kBackLeftDriveAbsoluteEncoderPort,
 * DriveConstants.kBackLeftDriveAbsoluteEncoderOffsetRad,
 * DriveConstants.kBackLeftDriveEncoderReversed,
 * DriveConstants.kBackLeftTurningEncoderReversed),
 * new SwerveModule(3,
 * DriveConstants.kBackRightDriveMotorPort,
 * DriveConstants.kBackRightTurningMotorPort,
 * DriveConstants.kBackRightDriveAbsoluteEncoderPort,
 * DriveConstants.kBackRightDriveAbsoluteEncoderOffsetRad,
 * DriveConstants.kBackRightDriveEncoderReversed,
 * DriveConstants.kBackRightTurningEncoderReversed)
 * };
 * 
 * odometry = new SwerveDriveOdometry(
 * DriveConstants.kDriveKinematics, getRotation2d(), getModulePositions());
 * SmartDashboard.putData("Field", field);
 * 
 * System.out.println("✅ Swerve inicializado");
 * }
 * 
 * public Rotation2d getRotation2d() {
 * if (navx.isConnected()) {
 * return Rotation2d.fromDegrees(-navx.getYaw());
 * }
 * return new Rotation2d();
 * }
 * 
 * public double getHeading() {
 * return Math.IEEEremainder(getRotation2d().getDegrees(), 360);
 * }
 * 
 * public void zeroHeading() {
 * if (navx.isConnected()) {
 * navx.zeroYaw();
 * System.out.println("🧭 Heading reseteado");
 * }
 * }
 * 
 * public void drive(double xSpeed, double ySpeed, double rotSpeed, boolean
 * fieldRelative) {
 * ChassisSpeeds chassisSpeeds;
 * if (fieldRelative) {
 * chassisSpeeds = ChassisSpeeds.fromFieldRelativeSpeeds(
 * xSpeed, ySpeed, rotSpeed, getRotation2d());
 * } else {
 * chassisSpeeds = new ChassisSpeeds(xSpeed, ySpeed, rotSpeed);
 * }
 * 
 * SwerveModuleState[] states =
 * DriveConstants.kDriveKinematics.toSwerveModuleStates(chassisSpeeds);
 * SwerveDriveKinematics.desaturateWheelSpeeds(states,
 * DriveConstants.kPhysicalMaxSpeedMetersPerSecond);
 * setModuleStates(states);
 * }
 * 
 * public void setModuleStates(SwerveModuleState[] states) {
 * for (int i = 0; i < 4; i++) {
 * swerveModules[i].setDesiredState(states[i]);
 * }
 * }
 * 
 * public void stopModules() {
 * for (SwerveModule module : swerveModules) {
 * module.stop();
 * }
 * }
 * 
 * public SwerveModulePosition[] getModulePositions() {
 * SwerveModulePosition[] positions = new SwerveModulePosition[4];
 * for (int i = 0; i < 4; i++) {
 * positions[i] = swerveModules[i].getPosition();
 * }
 * return positions;
 * }
 * 
 * // ===== ALINEACIÓN AUTOMÁTICA =====
 * 
 * public void alignWheelsToFront() {
 * System.out.println("\n⬆️ ALINEANDO LLANTAS AL FRENTE");
 * SwerveModuleState[] states = new SwerveModuleState[4];
 * for (int i = 0; i < 4; i++) {
 * states[i] = new SwerveModuleState(0, Rotation2d.fromDegrees(0));
 * }
 * setModuleStates(states);
 * }
 * 
 * public void autoCalibrateOffsets() {
 * System.out.println("\n=== CALIBRACIÓN AUTOMÁTICA DE OFFSETS ===");
 * System.out.
 * println("1. Gira CADA llanta MANUALMENTE hasta que apunte AL FRENTE");
 * System.out.println("2. Presiona OK cuando estén listas");
 * System.out.println("Esperando 5 segundos...");
 * 
 * Timer.delay(5.0);
 * 
 * System.out.println("\n=== NUEVOS OFFSETS (COPIA ESTOS VALORES) ===");
 * double[] rawValues = new double[4];
 * for (int i = 0; i < 4; i++) {
 * rawValues[i] = swerveModules[i].getRawAbsoluteEncoderRad();
 * System.out.println("Módulo " + i + ": " + String.format("%.6f", rawValues[i])
 * + " rad");
 * }
 * 
 * System.out.println("\n=== APLICANDO OFFSETS TEMPORALES ===");
 * for (int i = 0; i < 4; i++) {
 * swerveModules[i].setOffset(rawValues[i]);
 * System.out.println("Módulo " + i + " offset aplicado");
 * }
 * 
 * System.out.println("\n✅ Calibración completada");
 * System.out.
 * println("Los offsets se perderán al reiniciar. Copia los valores a Constants.java"
 * );
 * }
 * 
 * public void resetToStoredOffsets() {
 * System.out.println("\n=== RESTAURANDO OFFSETS GUARDADOS ===");
 * swerveModules[0].setOffset(DriveConstants.
 * kFrontLeftDriveAbsoluteEncoderOffsetRad);
 * swerveModules[1].setOffset(DriveConstants.
 * kFrontRightDriveAbsoluteEncoderOffsetRad);
 * swerveModules[2].setOffset(DriveConstants.
 * kBackLeftDriveAbsoluteEncoderOffsetRad);
 * swerveModules[3].setOffset(DriveConstants.
 * kBackRightDriveAbsoluteEncoderOffsetRad);
 * System.out.println("Offsets restaurados desde Constants.java");
 * }
 * 
 * public void printAllAngles() {
 * System.out.println("\n=== ÁNGULOS ACTUALES ===");
 * for (int i = 0; i < 4; i++) {
 * System.out.println("Módulo " + i + ": " +
 * String.format("%.1f", swerveModules[i].getAbsoluteEncoderDeg()) + "°");
 * }
 * }
 * 
 * // ===== MÉTODOS DE PRUEBA =====
 * 
 * public void testModuleTurn(int moduleIndex, double power) {
 * if (moduleIndex >= 0 && moduleIndex < 4) {
 * swerveModules[moduleIndex].turnRaw(power);
 * }
 * }
 * 
 * public void stopModuleTurn(int moduleIndex) {
 * if (moduleIndex >= 0 && moduleIndex < 4) {
 * swerveModules[moduleIndex].turnRaw(0);
 * }
 * }
 * 
 * @Override
 * public void periodic() {
 * odometry.update(getRotation2d(), getModulePositions());
 * field.setRobotPose(odometry.getPoseMeters());
 * 
 * SmartDashboard.putNumber("Heading", getHeading());
 * for (SwerveModule module : swerveModules) {
 * module.updateDashboard();
 * }
 * }
 * }
 */