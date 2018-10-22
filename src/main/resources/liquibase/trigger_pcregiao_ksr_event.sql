CREATE OR REPLACE TRIGGER TRIGGER_PCREGIAO_KSR_EVENT
AFTER INSERT OR DELETE OR UPDATE OF regiao, uf 
ON PCREGIAO
FOR EACH ROW
BEGIN 
    IF INSERTING OR UPDATING THEN
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :new.numregiao, 'Regiao', 'Save', null, 1, sysdate);      
    ELSE
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :old.numregiao, 'Regiao', 'Delete', ksr_fetch_record(:old.numregiao, 'Regiao'), 1, sysdate);
    END IF;
END;
