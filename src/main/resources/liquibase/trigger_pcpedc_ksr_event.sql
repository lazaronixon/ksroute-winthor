CREATE OR REPLACE TRIGGER TRIGGER_PCPEDC_KSR_EVENT
AFTER INSERT OR DELETE OR UPDATE OF data, vlatend, totpeso, totvolume, codfilial, codcli, posicao 
ON PCPEDC
FOR EACH ROW
BEGIN 
    IF INSERTING OR UPDATING THEN
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :new.numped, 'Pedido', 'save', null, 1, sysdate);      
    ELSE
        INSERT INTO ksr_event VALUES (ksr_event_seq.nextval, :old.numped, 'Pedido', 'delete', ksr_fetch_record(:old.numped, 'Pedido'), 1, sysdate);
    END IF;
END;
