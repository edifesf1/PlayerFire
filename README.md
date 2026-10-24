# PlayerFire — APK para Amazon Fire TV Stick

Projeto Android mínimo que consulta o primeiro endereço válido do arquivo:

`https://raw.githubusercontent.com/edifesf1/r/refs/heads/main/link.txt`

Em seguida, tenta abrir esse endereço no Amazon Silk (`com.amazon.cloud9`). Se o Silk não estiver disponível para receber o link, tenta o navegador padrão.

## Gerar APK pelo GitHub Actions

1. Crie/abra o repositório GitHub onde deseja guardar este projeto.
2. Envie todos os arquivos e pastas deste projeto para a raiz do repositório.
3. Confirme que a branch se chama `main`.
4. Abra **Actions** no GitHub e selecione **Gerar APK PlayerFire**.
5. Se necessário, clique em **Run workflow**.
6. Quando terminar, abra a execução concluída e baixe o artefato **PlayerFire-APK**.
7. Extraia o ZIP do artefato; o arquivo será `app-debug.apk`.

O workflow também executa automaticamente quando você envia alterações para `main`.

## Formato do link.txt

Coloque um endereço completo por linha. O aplicativo usa a primeira linha não vazia que não comece com `#`.

Exemplo:

`https://exemplo.com/pagina`

Não coloque aspas nem texto explicativo na mesma linha.

## Limitação importante do Amazon Silk

O APK solicita que o Silk abra o endereço. O Android não oferece ao APK controle confiável para esconder as barras internas do Silk ou forçar seu modo de tela cheia; isso depende do Silk e da versão do Fire OS. O modo imersivo configurado neste projeto vale para a tela do próprio APK, antes de abrir o navegador.

A logo fornecida está configurada como ícone do aplicativo. Para distribuição pública, considere criar uma versão assinada de release; o APK debug gerado aqui serve para teste e instalação manual.
