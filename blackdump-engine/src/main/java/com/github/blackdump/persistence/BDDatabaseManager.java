package com.github.blackdump.persistence;

import com.github.blackdump.annotations.ABDManager;
import com.github.blackdump.base.BaseManager;
import com.github.blackdump.interfaces.persistence.IBDDatabaseManager;
import com.github.blackdump.serializer.JsonSerializer;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Level;

import java.io.File;
import java.util.Date;


/**
 * Manager per la creazione del database
 */
@ABDManager
public class BDDatabaseManager  extends BaseManager implements IBDDatabaseManager {

    private BDDatabase mDatabase;

    private String databasePath;
    private String databaseFilename = "blackdump.db";


    @Override
    public void ready() {
        log(Level.INFO, "Check database..." );

        checkDefaultDatabase();

    }

    private void checkDefaultDatabase()
    {
        databasePath = getEngine().getRootDirectory() + "database" + File.separator;
        databaseFilename = databasePath + databaseFilename;

        if (!new File(databasePath).exists())
        {
            log(Level.DEBUG, "Creating default directory %s", databasePath);
            new File(databasePath).mkdirs();

        }

        if (!new File(databaseFilename).exists())
        {
            mDatabase = new BDDatabase();

            saveDatabase();
        }

      loadDatabase();
    }

    @Override
    public void loadDatabase() {
        try {
            mDatabase = JsonSerializer.Deserialize(FileUtils.readFileToString(new File(databaseFilename)), BDDatabase.class);

            log(Level.INFO, "Database loaded! %s", databaseFilename);
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during load database %s => %s", databaseFilename, ex.getMessage());
        }
    }

    @Override
    public BDDatabase getDatabase() {
        return mDatabase;
    }

    @Override
    public void saveDatabase()
    {
        try
        {
            mDatabase.setLastModifiedDate(new Date());
            FileUtils.writeStringToFile(new File(databaseFilename), JsonSerializer.Serialize(mDatabase));
        }
        catch (Exception ex)
        {
            log(Level.FATAL, "Error during save database %s => %s", databaseFilename, ex.getMessage());
        }
    }
}
