#### Update WSL & clean distributions

```ps
# Update
PS C:\WINDOWS\system32> wsl --update
PS C:\WINDOWS\system32> wsl --set-default-version 2

# Clean distribs
PS C:\WINDOWS\system32> wsl --install -d Ubuntu   
PS C:\WINDOWS\system32> wsl --unregister Legacy   

# List & Check 
PS C:\WINDOWS\system32> wsl -l -v         
  NAME              STATE           VERSION
* Ubuntu            Running         2
```
Files are located in `\\wsl$\Ubuntu\...` 

```bash
mkdir -p ~/projects
cd ~/projects
git clone ...
```