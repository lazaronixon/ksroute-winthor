CREATE OR REPLACE TRIGGER TRIGGER_PCFILIAL_KSR_EVENT
AFTER INSERT OR DELETE OR UPDATE OF razaosocial 
ON PCFILIAL
FOR EACH ROW
BEGIN 
    IF INSERTING OR UPDATING THEN
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :new.codigo, 'Filial', 'save', null, 1, sysdate);      
    ELSE
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :old.codigo, 'Filial', 'delete', ksr_fetch_record(:old.codigo, 'Filial'), 1, sysdate);
    END IF;
END;
