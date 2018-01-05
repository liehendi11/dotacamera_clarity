package skadistats.clarity.examples.lifestate;

import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skadistats.clarity.event.Insert;
import skadistats.clarity.model.Entity;
import skadistats.clarity.processor.runner.Context;
import skadistats.clarity.processor.runner.SimpleRunner;
import skadistats.clarity.source.MappedFileSource;


public class Main {

    private final Logger log = LoggerFactory.getLogger(Main.class.getPackage().getClass());

    @Insert
    private Context ctx;

    @OnEntitySpawned
    public void onSpawned(Entity e) {
        List<String> classes = Arrays.asList(
                "CDOTA_BaseNPC_Tower",
                "CDOTA_BaseNPC_Barracks",
                "CDOTA_BaseNPC_Fort",
                "CDOTA_BaseNPC_Healer",
                "CDOTA_Unit_Courier",
                "CDOTA_Unit_Roshan");
        
        
        if (classes.contains(e.getDtClass().getDtName())) {
            log.info("{},spawn,{},{},{},{},{},{},{},{}",
                    ctx.getTick(),
                    e.getIndex(),
                    e.getDtClass().getDtName(),
                    e.getProperty("m_iUnitNameIndex"),
                    e.getProperty("m_iTeamNum"),
                    e.getProperty("CBodyComponent.m_cellX"),
                    e.getProperty("CBodyComponent.m_cellY"),
                    e.getProperty("m_iHealth"),
                    e.getProperty("m_iMaxHealth"));
            
        }
        
        String hero_pattern = "CDOTA_Unit_Hero";
        if (e.getDtClass().getDtName().contains(hero_pattern)) {
            log.info("{},spawn,{},{},{},{},{},{},{},{}",
                    ctx.getTick(),
                    e.getIndex(),
                    e.getDtClass().getDtName(),
                    e.getProperty("m_iPlayerID"),
                    e.getProperty("m_iTeamNum"),
                    e.getProperty("CBodyComponent.m_cellX"),
                    e.getProperty("CBodyComponent.m_cellY"),
                    e.getProperty("m_iHealth"),
                    e.getProperty("m_iMaxHealth"));
        }
        
        if (e.getDtClass().getDtName() == "CDOTA_Unit_Courier") {
            log.info("{},spawn,{},{},{},{},{},{},{},{}",
                    ctx.getTick(),
                    e.getIndex(),
                    e.getDtClass().getDtName(),
                    e.getProperty("m_iUnitNameIndex"),
                    e.getProperty("m_iTeamNum"),
                    e.getProperty("CBodyComponent.m_cellX"),
                    e.getProperty("CBodyComponent.m_cellY"),
                    e.getProperty("m_iHealth"),
                    e.getProperty("m_iMaxHealth"));
        }
    }

    @OnEntityDied
    public void onDied(Entity e) {
        List<String> classes = Arrays.asList(
                "CDOTA_BaseNPC_Tower",
                "CDOTA_BaseNPC_Barracks",
                "CDOTA_BaseNPC_Fort",
                "CDOTA_BaseNPC_Healer",
                "CDOTA_Unit_Courier",
                "CDOTA_Unit_Roshan");
        
        if (classes.contains(e.getDtClass().getDtName())) {
            log.info("{},death,{},{},{},{},{},{},{},{}",
                    ctx.getTick(),
                    e.getIndex(),
                    e.getDtClass().getDtName(),
                    e.getProperty("m_iUnitNameIndex"),
                    e.getProperty("m_iTeamNum"),
                    e.getProperty("CBodyComponent.m_cellX"),
                    e.getProperty("CBodyComponent.m_cellY"),
                    e.getProperty("m_iHealth"),
                    e.getProperty("m_iMaxHealth"));
            
        }
        
        String hero_pattern = "CDOTA_Unit_Hero";
        if (e.getDtClass().getDtName().contains(hero_pattern)) {
            log.info("{},death,{},{},{},{},{},{},{},{}",
                    ctx.getTick(),
                    e.getIndex(),
                    e.getDtClass().getDtName(),
                    e.getProperty("m_iPlayerID"),
                    e.getProperty("m_iTeamNum"),
                    e.getProperty("CBodyComponent.m_cellX"),
                    e.getProperty("CBodyComponent.m_cellY"),
                    e.getProperty("m_iHealth"),
                    e.getProperty("m_iMaxHealth"));
        }
        
        if (e.getDtClass().getDtName() == "CDOTA_Unit_Courier") {
            log.info("{},death,{},{},{},{},{},{},{},{}",
                    ctx.getTick(),
                    e.getIndex(),
                    e.getDtClass().getDtName(),
                    e.getProperty("m_iUnitNameIndex"),
                    e.getProperty("m_iTeamNum"),
                    e.getProperty("CBodyComponent.m_cellX"),
                    e.getProperty("CBodyComponent.m_cellY"),
                    e.getProperty("m_iHealth"),
                    e.getProperty("m_iMaxHealth"));
        }
    }

    public void run(String[] args) throws Exception {
        long tStart = System.currentTimeMillis();
        SimpleRunner r = null;
        try {
            log.info("tick,action,index,class,unit_player_index,team_num,x,y,health,max_health");
            r = new SimpleRunner(new MappedFileSource(args[0])).runWith(this);
        } finally {
            long tMatch = System.currentTimeMillis() - tStart;
//            log.info("total time taken: {}s", (tMatch) / 1000.0);
            if (r != null) {
                r.getSource().close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new Main().run(args);
    }

}
