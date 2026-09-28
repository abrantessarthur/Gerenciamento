Problema:
Duas requisições simultâneas poderiam reservar o mesmo campo e horário.

Decisão:
Usar bloqueio pessimista na linha do campo e transação

Motivos:
O bloqueio organiza as requisições concorrentes.

Consequências:
As reservas do mesmo campo são processadas uma por vez.
Isso aumenta a segurança, mas pode reduzir o desempenho quando houver muita concorrência.