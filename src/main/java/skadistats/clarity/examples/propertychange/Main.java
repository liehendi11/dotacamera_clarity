package skadistats.clarity.examples.propertychange;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skadistats.clarity.model.Entity;
import skadistats.clarity.model.FieldPath;
import skadistats.clarity.processor.entities.OnEntityPropertyChanged;
import skadistats.clarity.processor.entities.UsesEntities;
import skadistats.clarity.processor.runner.Context;
import skadistats.clarity.processor.runner.SimpleRunner;
import skadistats.clarity.source.MappedFileSource;

@UsesEntities
public class Main {

    private final Logger log = LoggerFactory.getLogger(Main.class.getPackage().getClass());
    
    public void printPropertyChange(String event, Context ctx, Entity e, FieldPath fp) {
        
        log.info("{},{},{},{},{},{}",
                ctx.getTick(),
                event,
                e.getIndex(),
                e.getDtClass().getDtName(),
                e.getDtClass().getNameForFieldPath(fp),
                e.getPropertyForFieldPath(fp));
    }
    
    // hero - location
    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Hero_.*", propertyPattern = "CBodyComponent.m_cell[XY]")
    public void onEntityPropertyChanged(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_location", ctx, e, fp);
    }
    
    // hero - level
    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Hero_.*", propertyPattern = "m_iCurrentLevel")
    public void onLevel(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_level", ctx, e, fp);
    }
    
    // hero - alive
    @OnEntityPropertyChanged(classPattern = "CDOTA_PlayerResource", propertyPattern = "m_vecPlayerTeamData.*.m_iRespawnSeconds")
    public void onRespawn(Context ctx, Entity e, FieldPath fp) {
        if (e.getPropertyForFieldPath(fp) == "-1") {
            printPropertyChange("hero_respawn", ctx, e, fp);
        }
    }
    
    // tower - pos
    @OnEntityPropertyChanged(classPattern = "CDOTA_BaseNPC_Tower", propertyPattern = ".*")
    public void onTowerChange(Context ctx, Entity e, FieldPath fp) {
        List<String> components = Arrays.asList("11", "43", "14/0", "14/1");
        
        if (components.contains(fp.toString())) {
            printPropertyChange("tower", ctx, e, fp);
        }
    }
    
    // barrack - pos
    @OnEntityPropertyChanged(classPattern = "CDOTA_BaseNPC_Barracks", propertyPattern = ".*")
    public void onBarracksChange(Context ctx, Entity e, FieldPath fp) {
        List<String> components = Arrays.asList("11", "14/0", "14/1");
        if (components.contains(fp.toString())) {
            printPropertyChange("barracks", ctx, e, fp);
        }
    }
    
    // ancient - pos
    @OnEntityPropertyChanged(classPattern = "CDOTA_BaseNPC_Fort", propertyPattern = ".*")
    public void onFortChange(Context ctx, Entity e, FieldPath fp) {
        List<String> components = Arrays.asList("11", "43", "14/0", "14/1");
        if (components.contains(fp.toString())) {
            printPropertyChange("ancient", ctx, e, fp);
        }
    }
    
    // roshan - pos
    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Roshan", propertyPattern = ".*")
    public void onRoshanChange(Context ctx, Entity e, FieldPath fp) {
        List<String> components = Arrays.asList("11", "43", "14/0", "14/1");
        if (components.contains(fp.toString())) {
            printPropertyChange("roshan", ctx, e, fp);
        }
    }
    
    // courier - pos
    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Courier", propertyPattern = ".*")
    public void onCourierChange(Context ctx, Entity e, FieldPath fp) {
        List<String> components = Arrays.asList("11", "43", "14/0", "14/1");
        if (components.contains(fp.toString())) {
            printPropertyChange("courier", ctx, e, fp);
        }
    }
    
    // hero - health
    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Hero_.*", propertyPattern = "m_iHealth")
    public void onHeroHealthChange(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_health", ctx, e, fp);        
    }
    
    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Hero_.*", propertyPattern = "m_iMaxHealth")
    public void onHeroMaxHealthChange(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_health", ctx, e, fp);        
    }
    
    // hero - mana
    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Hero_.*", propertyPattern = "m_flMana")
    public void onHeroManaChange(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_mana", ctx, e, fp);
    }
    
    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Hero_.*", propertyPattern = "m_flMaxMana")
    public void onHeroMaxManaChange(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_mana", ctx, e, fp);
    }
    
    // hero - kill
    @OnEntityPropertyChanged(classPattern = "CDOTA_PlayerResource", propertyPattern = "m_vecPlayerTeamData.*.m_iKills")
    public void onHeroKillChange(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_kill", ctx, e, fp);
    }
    
    // hero - death
    @OnEntityPropertyChanged(classPattern = "CDOTA_PlayerResource", propertyPattern = "m_vecPlayerTeamData.*.m_iDeaths")
    public void onHeroDeathChange(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_death", ctx, e, fp);
    }
    
    // hero - assists
    @OnEntityPropertyChanged(classPattern = "CDOTA_PlayerResource", propertyPattern = "m_vecPlayerTeamData.*.m_iAssists")
    public void onHeroDeathAssists(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_assist", ctx, e, fp);
    }
    
    // radiant - gold
    @OnEntityPropertyChanged(classPattern = "CDOTA_DataRadiant", propertyPattern = "m_vecDataTeam.*.m_iNetWorth")
    public void onRadiantHeroNetworth(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_networth", ctx, e, fp);
    }
    
    // dire - gold
    @OnEntityPropertyChanged(classPattern = "CDOTA_DataDire", propertyPattern = "m_vecDataTeam.*.m_iNetWorth")
    public void onDireHeroNetworth(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("hero_networth", ctx, e, fp);
    }
    
    
    
    public void run(String[] args) throws Exception {
        log.info("tick,event,index,class,property,value");
        long tStart = System.currentTimeMillis();
        new SimpleRunner(new MappedFileSource(args[0])).runWith(this);
        long tMatch = System.currentTimeMillis() - tStart;
        
    }

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

}
