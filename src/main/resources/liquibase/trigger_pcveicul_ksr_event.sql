CREATE OR REPLACE TRIGGER TRIGGER_PCVEICUL_KSR_EVENT
AFTER INSERT OR DELETE OR UPDATE OF descricao
ON PCVEICUL
FOR EACH ROW
BEGIN 
    IF INSERTING OR UPDATING THEN
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :new.codveiculo, 'Veiculo', 'save', null, 1, sysdate);      
    ELSE
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :old.codveiculo, 'Veiculo', 'delete', ksr_fetch_record(:old.codveiculo, 'Veiculo'), 1, sysdate);
    END IF;
END;
