package ke.don.datasource.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_3 = object : Migration(1, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Create the 'sessions' table with the new schema
        database.execSQL("""
            CREATE TABLE IF NOT EXISTS `sessions` (
                `sessionId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                `timerId` INTEGER NOT NULL,
                `timerName` TEXT NOT NULL,
                `startTime` INTEGER NOT NULL,
                `endTime` INTEGER NOT NULL,
                `expectedRunTime` INTEGER NOT NULL,
                `totalDuration` INTEGER NOT NULL,
                `status` TEXT NOT NULL,
                FOREIGN KEY(`timerId`) REFERENCES `timer`(`timerId`)
            )
        """)
    }
}
