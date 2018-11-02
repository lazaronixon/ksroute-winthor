CREATE OR REPLACE TRIGGER TRIGGER_PCROTAEXP_KSR_EVENT
AFTER INSERT OR DELETE OR UPDATE OF descricao
ON PCROTAEXP
FOR EACH ROW
BEGIN 
    IF INSERTING OR UPDATING THEN
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :new.codrota, 'Rota', 'save', null, 1, sysdate);      
    ELSE
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :old.codrota, 'Rota', 'delete', ksr_fetch_record(:old.codrota, 'Rota'), 1, sysdate);
    END IF;
END;
