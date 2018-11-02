CREATE OR REPLACE TRIGGER TRIGGER_PCPRACA_KSR_EVENT
AFTER INSERT OR DELETE OR UPDATE OF praca, numregiao, rota 
ON PCPRACA
FOR EACH ROW
BEGIN 
    IF INSERTING OR UPDATING THEN
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :new.codpraca, 'Praca', 'save', null, 1, sysdate);      
    ELSE
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :old.codpraca, 'Praca', 'delete', ksr_fetch_record(:old.codpraca, 'Praca'), 1, sysdate);
    END IF;
END;
