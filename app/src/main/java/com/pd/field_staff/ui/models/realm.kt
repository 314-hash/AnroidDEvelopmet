package com.pd.field_staff.ui.models

import io.realm.kotlin.migration.AutomaticSchemaMigration
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid



class JobEntry: RealmObject {

    @OptIn(ExperimentalUuidApi::class)
    @PrimaryKey
    var uuid: String = Uuid.random().toString()

}


class StaffRealmMigration: AutomaticSchemaMigration {


    override fun migrate(migrationContext: AutomaticSchemaMigration.MigrationContext) {


    }
}