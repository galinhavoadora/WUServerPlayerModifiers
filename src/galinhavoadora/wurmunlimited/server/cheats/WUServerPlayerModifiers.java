package galinhavoadora.wurmunlimited.server.cheats;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.gotti.wurmunlimited.modloader.classhooks.HookException;
import org.gotti.wurmunlimited.modloader.classhooks.HookManager;
import org.gotti.wurmunlimited.modloader.interfaces.Configurable;
import org.gotti.wurmunlimited.modloader.interfaces.PreInitable;
import org.gotti.wurmunlimited.modloader.interfaces.WurmServerMod;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtPrimitiveType;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;

public class WUServerPlayerModifiers implements Configurable, PreInitable, WurmServerMod {
	
	private Logger _logger = Logger.getLogger(this.getClass().getName());
	private boolean multiplyPlayerAddMoney;
	private static long playerAddMoneyMultiplier = 10L;
	private boolean alwaysSleepBonus;
	private boolean noMoveRestriction;
	private boolean noPlayerDamage;
	private boolean noPlayerStaminaChange;
	private boolean noPlayerThirst;
	private boolean noPlayerHunger;
	private static float speedModifier = 1.0f;

	@Override
	public void preInit() {
		if (multiplyPlayerAddMoney) 
			this.MultiplyPlayerAddMoney();
		if (alwaysSleepBonus)
			this.AlwaysSleepBonus();
		if (noMoveRestriction)
			this.NoMoveRestriction();
		if (noPlayerDamage)
			this.NoPlayerDamage();
		if (noPlayerStaminaChange)
			this.NoPlayerStaminaChange();
		if (noPlayerThirst)
			this.NoPlayerThirst();
		if (noPlayerHunger)
			this.NoPlayerHunger();
		this.lockSpeedModifier();
	}

	@Override
	public void configure(Properties properties) {
		this.multiplyPlayerAddMoney = Boolean.valueOf(properties.getProperty("multiplyPlayerAddMoney", Boolean.toString(this.multiplyPlayerAddMoney))).booleanValue();
        this.Log("multiplyPlayerAddMoney On: ", this.multiplyPlayerAddMoney);
        this.playerAddMoneyMultiplier = Long.valueOf(properties.getProperty("playerAddMoneyMultiplier", Boolean.toString(this.multiplyPlayerAddMoney))).longValue();
        this.alwaysSleepBonus = Boolean.valueOf(properties.getProperty("alwaysSleepBonus", Boolean.toString(this.alwaysSleepBonus))).booleanValue();
        this.Log("alwaysSleepBonus On: ", this.alwaysSleepBonus);
        this.noMoveRestriction = Boolean.valueOf(properties.getProperty("noMoveRestriction", Boolean.toString(this.noMoveRestriction))).booleanValue();
        this.Log("noMoveRestriction On: ", this.noMoveRestriction);
        this.noPlayerDamage = Boolean.valueOf(properties.getProperty("noPlayerDamage", Boolean.toString(this.noPlayerDamage))).booleanValue();
        this.Log("noPlayerDamage On: ", this.noPlayerDamage);
        this.noPlayerStaminaChange = Boolean.valueOf(properties.getProperty("noPlayerStaminaChange", Boolean.toString(this.noPlayerStaminaChange))).booleanValue();
        this.Log("noPlayerStaminaChange On: ", this.noPlayerStaminaChange);
        this.noPlayerThirst = Boolean.valueOf(properties.getProperty("noPlayerThirst", Boolean.toString(this.noPlayerThirst))).booleanValue();
        this.Log("noPlayerThirst On: ", this.noPlayerThirst);
        this.noPlayerHunger = Boolean.valueOf(properties.getProperty("noPlayerHunger", Boolean.toString(this.noPlayerHunger))).booleanValue();
        this.Log("noPlayerHunger On: ", this.noPlayerHunger);
        this.speedModifier = Float.valueOf(properties.getProperty("speedModifier", Float.toString(this.speedModifier))).floatValue();        
	}
	
	private void Log(String forFeature, boolean activated) {
        this._logger.log(Level.INFO, forFeature + activated);
    }
	
	private void MultiplyPlayerAddMoney() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.players.Player");
            CtClass[] parameters = new CtClass[]{CtPrimitiveType.longType};
            CtMethod method = ex.getMethod("addMoney", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method.insertBefore("$1 = $1 * 10L;");
            //method.setBody("return true;");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}
	
	private void AlwaysSleepBonus() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.players.Player");
            ClassFile cf = ex.getClassFile();
            CtClass[] parameters = new CtClass[]{};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method.setBody("return true;");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}
	
	private void NoMoveRestriction() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.Creature");
            ClassFile cf = ex.getClassFile();
            CtClass[] parameters = new CtClass[]{};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            CtMethod method = ex.getMethod("isMoveSlow", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method.insertBefore("if (this.isPlayer()) {\n"
            		+ "return false;\n"
            		+ "}");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            method = ex.getMethod("isCantMove", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method.insertBefore("if (this.isPlayer()) {\n"
            		+ "return false;\n"
            		+ "}");
            method = ex.getMethod("isEncumbered", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method.insertBefore("if (this.isPlayer()) {\n"
            		+ "return false;\n"
            		+ "}");
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}
	
	private void NoPlayerDamage() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.CreatureStatus");
            ClassFile cf = ex.getClassFile();
            CtClass[] parameters = new CtClass[]{CtPrimitiveType.intType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            CtMethod method = ex.getMethod("modifyWounds", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method.insertBefore("if (this.statusHolder.isPlayer() && $1 > 0) {\n"
            		+ "$1 = 0;\n"
            		+ "}");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}
	
	private void NoPlayerStaminaChange() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.CreatureStatus");
            ClassFile cf = ex.getClassFile();
            CtClass[] parameters = new CtClass[]{CtPrimitiveType.floatType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            CtMethod method = ex.getMethod("modifyStamina", Descriptor.ofMethod(CtPrimitiveType.voidType, parameters));
            method.insertBefore("if (this.statusHolder.isPlayer() && $1 < 0.0f) {\n"
            		+ "$1 = 0.0f;\n"
            		+ "}");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            parameters = new CtClass[]{CtPrimitiveType.floatType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method = ex.getMethod("modifyStamina2", Descriptor.ofMethod(CtPrimitiveType.voidType, parameters));
            method.insertBefore("if (this.statusHolder.isPlayer() && $1 < 0.8f) {\n"
            		+ "$1 = 0.8f;\n"
            		+ "}");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);            
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}
	
	private void NoPlayerThirst() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.CreatureStatus");
            ClassFile cf = ex.getClassFile();
            CtClass[] parameters = new CtClass[]{CtPrimitiveType.floatType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            CtMethod method = ex.getMethod("modifyThirst", Descriptor.ofMethod(CtPrimitiveType.intType, parameters));
            method.insertBefore("if (this.statusHolder.isPlayer() && $1 > 0.0f) {\n"
            		+ "return this.thirst;\n"
            		+ "}");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            parameters = new CtClass[]{CtPrimitiveType.floatType, CtPrimitiveType.floatType
            		, CtPrimitiveType.floatType, CtPrimitiveType.floatType, CtPrimitiveType.floatType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method = ex.getMethod("modifyThirst", Descriptor.ofMethod(CtPrimitiveType.intType, parameters));
            method.insertBefore("if (this.statusHolder.isPlayer() && $1 > 0.0f) {\n"
            		+ "return this.thirst;\n"
            		+ "}");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);            
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}
	
	private void NoPlayerHunger() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.server.creatures.CreatureStatus");
            ClassFile cf = ex.getClassFile();
            CtClass[] parameters = new CtClass[]{CtPrimitiveType.intType, CtPrimitiveType.floatType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            CtMethod method = ex.getMethod("modifyHunger", Descriptor.ofMethod(CtPrimitiveType.intType, parameters));
            method.insertBefore("if (this.statusHolder.isPlayer() && $1 > 0) {\n"
            		+ "return this.hunger;\n"
            		+ "}");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            parameters = new CtClass[]{CtPrimitiveType.intType, CtPrimitiveType.floatType, CtPrimitiveType.floatType
            		, CtPrimitiveType.floatType, CtPrimitiveType.floatType, CtPrimitiveType.floatType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method = ex.getMethod("modifyHunger", Descriptor.ofMethod(CtPrimitiveType.intType, parameters));
            method.insertBefore("if (this.statusHolder.isPlayer() && $1 > 0) {\n"
            		+ "return this.hunger;\n"
            		+ "}");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);            
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}
	
	private void lockSpeedModifier() {
		try {
            CtClass ex = HookManager.getInstance().getClassPool().get("com.wurmonline.shared.util.MovementChecker");
            ClassFile cf = ex.getClassFile();
            CtClass[] parameters = new CtClass[]{CtPrimitiveType.floatType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            CtMethod method = ex.getMethod("setSpeedModifier", Descriptor.ofMethod(CtPrimitiveType.voidType, parameters));
            method.insertBefore("if ($1 < "+speedModifier+") $1 = "+speedModifier+";");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            parameters = new CtClass[]{};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method = ex.getMethod("getSpeedMod", Descriptor.ofMethod(CtPrimitiveType.floatType, parameters));
            method.insertBefore("if (this.speedMod < "+speedModifier+") return "+speedModifier+";");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            
            parameters = new CtClass[]{CtPrimitiveType.intType, CtPrimitiveType.intType, CtPrimitiveType.intType};
            //CtMethod method = ex.getMethod("hasSleepBonus", Descriptor.ofMethod(CtPrimitiveType.booleanType, parameters));
            method = ex.getMethod("getSpeedForTile", Descriptor.ofMethod(CtPrimitiveType.floatType, parameters));
            method.setBody("return 1.0f;");
            //methodInfo.rebuildStackMapIf6(classPool, cf);
            //methodInfo.rebuildStackMap(classPool);
            method = null;
            parameters = null;
            ex = null;
        } catch (NotFoundException | CannotCompileException var4) {
            throw new HookException(var4);
        }
	}

}
