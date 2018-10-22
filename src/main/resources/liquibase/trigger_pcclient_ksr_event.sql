CREATE OR REPLACE TRIGGER TRIGGER_PCCLIENT_KSR_EVENT
AFTER INSERT OR DELETE OR UPDATE OF cliente, fantasia, estent, municent, bairroent, enderent, cepent, codpraca
ON PCCLIENT
FOR EACH ROW
BEGIN 
    IF INSERTING OR UPDATING THEN
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :new.codcli, 'Cliente', 'Save', null, 1, sysdate);      
    ELSE
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :old.codcli, 'Cliente', 'Delete', ksr_fetch_record(:old.codcli, 'Cliente'), 1, sysdate);
    END IF;
END;
