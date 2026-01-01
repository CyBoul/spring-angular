#!/bin/bash
set -e

# 1. System update
sudo apt update
sudo apt upgrade -y
sudo apt install -y curl build-essential

# 2. Install NVM (Node Version Manager)
if [ ! -d "$HOME/.nvm" ]; then
    echo "Installing NVM..."
    curl -o- https://raw.githubusercontent.com/nvm-sh/nvm/v0.39.6/install.sh | bash
fi

# Load NVM in current SHELL
export NVM_DIR="$HOME/.nvm"
[ -s "$NVM_DIR/nvm.sh" ] && \. "$NVM_DIR/nvm.sh"

# 3. Install Node v 24.6.0
nvm install 24.6.0
nvm use 24.6.0
nvm alias default 24.6.0

echo "Node version:"
node -v
echo "npm version:"
npm -v

# 4. Install npm v 11.5.2
npm install -g npm@11.5.2
echo "npm updated version:"
npm -v

# 5. Install Angular CLI v 20.2.1
npm install -g @angular/cli@20.2.1
echo "Angular CLI version:"
ng version

echo "Installation complete. Node, npm and Angular are ready to use."