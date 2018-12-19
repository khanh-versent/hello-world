# Git Signed Commit Steps in MacOS

## Install GPG package

For easily install GPG and other packages needed for development, use Package manager tool **Homebrew**, either
* Download via [Homebrew homepage](https://brew.sh/)
* Use this command in Terminal
```
/usr/bin/ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)"
```

Then you could install GPG package via Homebrew by this command in Terminal
```
brew install gpg
```

## GPG key generation and management
If you install GPG package for the first time, you probably don't have any GPG key in the system. To generate a key, use this command in Terminal
```
gpg --generate-key
```
For ease of use with Git account, use the same email address registered with Git. Otherwise, you will need to add the mail into Git email. 

1. Github → Settings → Emails
2. Add email into Add email address textbook and click Add

## Put the key into your Git account
Follow those steps to put your GPG key into your Git account

1. Navigate to Github → Settings → SSH-Keys & GPG Keys - Github SSH & GPG Key Settings
2. Click on 'New GPG Key'

Use the Output of the below command in the text box: