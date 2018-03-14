:: 删除当前目录及子目录下所有.svn的内容

for /r . %%a in (.) do @if exist "%%a\.svn" rd /s /q "%%a\.svn"