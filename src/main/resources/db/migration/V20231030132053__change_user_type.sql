ALTER TABLE "user"
    ALTER COLUMN type TYPE VARCHAR(255);

UPDATE "user" SET type = 'OWNER' WHERE type = 'EMPLOYEE';

ALTER TABLE "user"
    ADD CONSTRAINT type_chk CHECK (type IN ('ADMIN', 'OWNER', 'STORE_MANAGER', 'STAFF', 'CUSTOMER'));

DROP CAST (CHARACTER VARYING AS user_type);
DROP TYPE user_type;