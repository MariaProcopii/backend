package com.training.license.sharing.config.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import static org.hibernate.boot.model.naming.Identifier.toIdentifier;

public class PluralizedPhysicalNamingStrategyStandardImpl extends PhysicalNamingStrategyStandardImpl {

    private static final String ERROR = "The table name is null!";

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        if (name != null) {
            return toIdentifier(name.getText() + "s");
        }
        throw new HibernateException(ERROR);
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier logicalName, JdbcEnvironment context) {
        if (logicalName != null) {
            return toIdentifier(logicalName.getText().toLowerCase());
        }
        throw new HibernateException(ERROR);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier logicalName, JdbcEnvironment context) {

        if (logicalName != null) {
            return toIdentifier(logicalName.getText().toLowerCase().split("_")[0] + "_id_seq");
        }
        throw new HibernateException("Sequence is null!");
    }
}