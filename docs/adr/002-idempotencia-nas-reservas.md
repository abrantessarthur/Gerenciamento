Problema: Retries podem repetir uma reserva já processada 

Decisão: Receber Idempotency-Key e persistir uma chave única 

Regra: Mesma chave e mesmos dados retorna a reserva existente.
       Mesma chave e dados diferentes retorna 409 Conflict.
       Existe uma restrição UNIQUE no banco 

Consequência: O cliente precisa gerar uma chave nova para cada operação 

Limitação: O cliente é responsável por gerar e reutilizar corretamente a chave 