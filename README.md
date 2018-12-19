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

To list all the GPG key in the system, in Terminal
```
gpg --list-keys
```
The response will be
```
gpg --list-keys
/Users/user/.gnupg/pubring.kbx
-------------------------------------
pub   rsa4096 2018-12-18 [SC] [expires: 2019-12-18]
      **[Public Key]**
uid           [ultimate] ....
sub   rsa4096 2018-12-18 [E] [expires: 2019-12-18]
```
To export the key, use the command
```
gpg --armor --export [keyword]
```
[keyword] could be the public key or a part of full name when you generate the new key.
The key will be print out in the Terminal console
```
-----BEGIN PGP PUBLIC KEY BLOCK-----

........
-----END PGP PUBLIC KEY BLOCK-----
```


To export the key into file for sending to others, use the command
```
gpg --armor --export [keyword] > [filename].asc
```


## Put the key into your Git account
Follow those steps to put your GPG key into your Git account

1. Navigate to Github → Settings → SSH-Keys & GPG Keys - Github SSH & GPG Key Settings
2. Click on 'New GPG Key'