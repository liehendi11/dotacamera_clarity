/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skadistats.clarity.examples.all_data;

import org.joda.time.Duration;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skadistats.clarity.event.Insert;
import skadistats.clarity.model.CombatLogEntry;
import skadistats.clarity.processor.runner.Context;
import skadistats.clarity.processor.gameevents.OnCombatLogEntry;
import skadistats.clarity.processor.runner.SimpleRunner;
import skadistats.clarity.source.MappedFileSource;
import skadistats.clarity.wire.common.proto.DotaUserMessages;


/**
 *
 * @author Hendi
 */
public class Main {
    private final Logger log = LoggerFactory.getLogger(Main.class.getPackage().getClass());
    
    @Insert
    private Context ctx;
    
    private String compileName(String name) {
        return name != null ? name : "UNKNOWN";
    }

    private String getAttackerNameCompiled(CombatLogEntry cle) {
        return compileName(cle.getAttackerName());
    }
    
    private String getAttackerTeam(CombatLogEntry cle) {
        return compileName(String.valueOf(cle.getAttackerTeam()));
    }

    private String getTargetNameCompiled(CombatLogEntry cle) {
        return compileName(cle.getTargetName());
    }
    
    private String getTargetTeam(CombatLogEntry cle) {
        return compileName(String.valueOf(cle.getTargetTeam()));
    }
    
    private String getDamageSource(CombatLogEntry cle) {
        return compileName(String.valueOf(cle.getDamageSourceName()));
    }
    
    
    private boolean getSourceIllusion(CombatLogEntry cle) {
        return cle.isAttackerIllusion();
    }
    
    private boolean getTargetIllusion(CombatLogEntry cle) {
        return cle.isTargetIllusion();
    }
    
    @OnCombatLogEntry
    public void onCombatLogEntry(CombatLogEntry cle) {
//        String time = "[" + GAMETIME_FORMATTER.print(Duration.millis((int) (1000.0f * cle.getTimestamp())).toPeriod()) + "]";
        String time = String.valueOf(ctx.getTick());
        
        String event = "";
        String source = "";
        boolean is_source_illu = cle.isAttackerIllusion();
        String source_team = "";
        String target = "";
        boolean is_target_illu = cle.isTargetIllusion();
        String target_team = "";
        String inflictor = "";
        String value = "";
        String before = "";
        String after = "";
        String dmg_source = "";
        boolean use = true;
        
        switch (cle.getType()) {
            case DOTA_COMBATLOG_DAMAGE:
                event = "damage";
                source = getAttackerNameCompiled(cle);
                target = getTargetNameCompiled(cle);
                
                source_team = getAttackerTeam(cle);
                target_team = getTargetTeam(cle);
                
                inflictor = cle.getInflictorName() != null ? cle.getInflictorName() : "";
                value = String.format("%s", cle.getValue());
                before = String.format("%s", cle.getHealth() + cle.getValue());
                after = String.format("%s", cle.getHealth());
                
                dmg_source = String.format("%s", cle.getDamageSourceName());
        
                break;
            case DOTA_COMBATLOG_HEAL:
                event = "heal";
                source = getAttackerNameCompiled(cle);
                target = getTargetNameCompiled(cle);
                inflictor = cle.getInflictorName();
                value = String.format("%s", cle.getValue());
                before = String.format("%s", cle.getHealth() - cle.getValue());
                after = String.format("%s", cle.getHealth());
                
                break;
            case DOTA_COMBATLOG_MODIFIER_ADD:
                
                event = "modifier_add";
                source = getAttackerNameCompiled(cle);
                target = getTargetNameCompiled(cle);
                inflictor = cle.getInflictorName();
                break;
            case DOTA_COMBATLOG_MODIFIER_REMOVE:
                event = "modifier_remove";
                target = getTargetNameCompiled(cle);
                inflictor = cle.getInflictorName();
              
                break;
            case DOTA_COMBATLOG_DEATH:
                event = "death";
                source = getAttackerNameCompiled(cle);
                target = getTargetNameCompiled(cle);
                
                dmg_source = String.format("%s", cle.getDamageSourceName());
                
                break;
            case DOTA_COMBATLOG_ABILITY:
                String toggle_cast = cle.isAbilityToggleOn() || cle.isAbilityToggleOff() ? "ability_toggle" : "ability_cast";
                toggle_cast += cle.isAbilityToggleOn() ? "_on" : cle.isAbilityToggleOff() ? "_off" : "";
                
                event = toggle_cast;
                source = getAttackerNameCompiled(cle);
                target = cle.getTargetName() != null ? getTargetNameCompiled(cle) : "";
                inflictor = cle.getInflictorName();
                value = String.valueOf(cle.getAbilityLevel());
                
                break;
            case DOTA_COMBATLOG_ITEM:
                event = "item";
                source = getAttackerNameCompiled(cle);
                target = cle.getTargetName() != null ? getTargetNameCompiled(cle) : "";
                inflictor = cle.getInflictorName();
                
                break;
            case DOTA_COMBATLOG_GOLD:
                
                event = "gold";
                target = getTargetNameCompiled(cle);
                value = String.valueOf(cle.getValue());
                
                break;
            case DOTA_COMBATLOG_GAME_STATE:
                event = "game_state";
                value = String.valueOf(cle.getValue());
                
                break;
            case DOTA_COMBATLOG_XP:
                event = "xp";
                target = getTargetNameCompiled(cle);
                value = String.valueOf(cle.getValue());
                break;
            case DOTA_COMBATLOG_PURCHASE:
                event = "purchase";
                target = getTargetNameCompiled(cle);
                value = cle.getValueName();
                break;
            case DOTA_COMBATLOG_BUYBACK:
                event = "purchase";
                value = String.valueOf(cle.getValue());
                break;
            case DOTA_COMBATLOG_AEGIS_TAKEN:
                event = "aegis";
                inflictor = cle.toString();
                break;
            default:
                DotaUserMessages.DOTA_COMBATLOG_TYPES type = cle.getType();
                event = "other";
                value = String.valueOf(type.ordinal());
                inflictor = cle.toString();
                use = false;
                break;

        }
        
        if (event != "other") {
            log.info("{},{},{},{},{},{},{},{},{},{},{},{},{}",
                time,
                event,
                source,
                is_source_illu,
                source_team,
                target,
                is_target_illu,
                target_team,
                inflictor,
                value,
                before,
                after,
                dmg_source
            );
        }
    }
    
    
    public void run(String[] args) throws Exception {
        log.info("tick,event,source,is_source_illu,source_team,target,"
                + "is_target_illu,target_team,inflictor,value,before,after,dmg_source");
        long tStart = System.currentTimeMillis();
        new SimpleRunner(new MappedFileSource(args[0])).runWith(this);
        long tMatch = System.currentTimeMillis() - tStart;
        
//        log.info("total time taken: {}s", (tMatch) / 1000.0);
    }

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }
            
}
