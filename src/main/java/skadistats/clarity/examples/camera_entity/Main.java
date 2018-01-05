/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skadistats.clarity.examples.camera_entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import skadistats.clarity.decoder.Util;
import skadistats.clarity.model.EngineType;
import skadistats.clarity.model.Entity;
import skadistats.clarity.model.FieldPath;
import skadistats.clarity.processor.entities.Entities;
import skadistats.clarity.processor.entities.UsesEntities;
import skadistats.clarity.processor.runner.SimpleRunner;
import skadistats.clarity.source.MappedFileSource;
import skadistats.clarity.util.TextTable;

import java.io.IOException;
import skadistats.clarity.processor.entities.OnEntityPropertyChanged;
import skadistats.clarity.processor.runner.Context;
/**
 *
 * @author Hendi
 */

@UsesEntities
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class.getPackage().getClass());
    private final SimpleRunner runner;
    
    private String getEngineDependentEntityName(String entityName) {
        switch (runner.getEngineType()) {
            case SOURCE1:
                return "DT_DOTA_" + entityName;
            case SOURCE2:
                return "CDOTA_" + entityName;
            default:
                throw new RuntimeException("invalid engine type");
        }
    }
    
    public void printPropertyChange(String event, Context ctx, Entity e, FieldPath fp) {
        log.info("{},{},{},{},{}",
                event,
                ctx.getTick(),
                e.getDtClass().getDtName(),
                e.getDtClass().getNameForFieldPath(fp),
                e.getPropertyForFieldPath(fp));
    }
    
    // hero - position
    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Hero_.*", propertyPattern = "CBodyComponent.m_cell*")
    public void onHeroLocationChange(Context ctx, Entity e, FieldPath fp) {
        printPropertyChange("location", ctx, e, fp);
//        return;
    }
    
    // hero - health
//    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Hero_.*", propertyPattern = "m_iHealth")
//    public void onHeroHealthChange(Context ctx, Entity e, FieldPath fp) {
//        printPropertyChange("health", ctx, e, fp);
//    }
//    
//    // hero - mana
//    @OnEntityPropertyChanged(classPattern = "CDOTA_Unit_Hero_.*", propertyPattern = "m_flMana")
//    public void onHeroManaChange(Context ctx, Entity e, FieldPath fp) {
//        printPropertyChange("mana", ctx, e, fp);
//    }
    
    private Entity getEntity(String entityName) {
        return runner.getContext().getProcessor(Entities.class).getByDtName(getEngineDependentEntityName(entityName));
    }
    
    public Main(String fileName) throws IOException, InterruptedException {
        runner = new SimpleRunner(new MappedFileSource(fileName)).runWith(this);
    }
}
