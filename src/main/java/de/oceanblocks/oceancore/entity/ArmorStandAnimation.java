package de.oceanblocks.oceancore.entity;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;

public class ArmorStandAnimation extends BukkitRunnable {

    ArmorStand s;
    float x;
    float y;
    float z;
    Rotation rotation;


    public ArmorStandAnimation(ArmorStand stand, float xPos, float yPos, float zPos, Rotation rotationPoint) {
        s = stand;
        x = xPos;
        y = yPos;
        z = zPos;
        rotation = rotationPoint;
    }


    @Override
    public void run() {
        if (Bukkit.getOnlinePlayers().size() >= 1) {
            switch (rotation) {
                case HEAD -> {
                    EulerAngle oldRot = s.getHeadPose();
                    EulerAngle newRot = oldRot.add(x, y, z);
                    s.setHeadPose(newRot);
                }
                case BODY -> {
                    EulerAngle oldRot2 = s.getBodyPose();
                    EulerAngle newRot2 = oldRot2.add(x, y, z);
                    s.setBodyPose(newRot2);
                }
                case LEFT_ARM -> {
                    EulerAngle oldRot3 = s.getLeftArmPose();
                    EulerAngle newRot3 = oldRot3.add(x, y, z);
                    s.setLeftArmPose(newRot3);
                }
                case RIGHT_ARM -> {
                    EulerAngle oldRot4 = s.getRightArmPose();
                    EulerAngle newRot4 = oldRot4.add(x, y, z);
                    s.setRightArmPose(newRot4);
                }
                case LEFT_LEG -> {
                    EulerAngle oldRot5 = s.getLeftLegPose();
                    EulerAngle newRot5 = oldRot5.add(x, y, z);
                    s.setLeftLegPose(newRot5);
                }
                case RIGHT_LEG -> {
                    EulerAngle oldRot6 = s.getRightLegPose();
                    EulerAngle newRot6 = oldRot6.add(x, y, z);
                    s.setRightLegPose(newRot6);
                }
            }
        }
    }

    public enum Rotation {
        HEAD,
        BODY,
        LEFT_ARM,
        RIGHT_ARM,
        LEFT_LEG,
        RIGHT_LEG;
    }

}
