AutoPesquisaEdge - Projeto Android Studio (Kotlin)
================================================

O que contém:
- App que abre o Microsoft Edge e realiza 30 pesquisas sequenciais com 10s entre cada.
- UI com botões: Iniciar agora, Agendar diariamente, Parar agendamento.
- Logs exibidos na tela (persistidos via SharedPreferences).

Como usar:
1. Baixe o ZIP e extraia.
2. Abra o diretório 'AutoPesquisaEdge' no Android Studio (File > Open).
3. Deixe o Android Studio baixar o Gradle/SDK necessário.
4. Conecte um dispositivo Android com o Microsoft Edge instalado ou use um emulador com Edge (Edge deve estar instalado).
5. Build > Run app.
6. Toque em "Iniciar agora" para executar 30 pesquisas imediatamente.
   - O app abre o Microsoft Edge (usando o esquema microsoft-edge:) para cada pesquisa.
7. Toque em "Agendar diariamente" para que o WorkManager execute o mesmo job a cada 24 horas.
   - Observação: PeriodicWorkRequest tem janelas de execução controladas pelo sistema; pode não rodar exatamente no mesmo horário.

Importante:
- Se o Microsoft Edge não estiver instalado, o app registrará nos logs a falha e não abrirá outro navegador.
- Este projeto é fornecido para fins de estudo/teste. Automatizar pesquisas em massa pode violar termos de serviço de alguns serviços.

Compilar APK:
- No Android Studio: Build > Build Bundle(s) / APK(s) > Build APK(s)
- O arquivo .apk será gerado no diretório 'app/build/outputs/apk/'.

Boa sorte e qualquer dúvida me avise!
