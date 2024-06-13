# Microchip PIC64GX Yocto BSP

Collection of OpenEmbedded/Yocto layers for PIC64GX.

- **meta-pic64gx-bsp**: layer containing PIC64GX's evaluation boards' metadata such as machine configuration files and core recipes (Linux, U-Boot, etc.).

- **meta-pic64gx-community**: layer containing Microchip's partners' evaluation kits' machine configuration files and associated configuration fragments.

- **meta-pic64gx-extras**: layer containing recipes that extend and supplement the meta-pic64gx-bsp layer. These include additional applications and demos to demonstrate PIC64GX features.

The complete User Guides for each development platform, containing board and boot instructions, are available for the following supported platforms:

- [CURIOSITY-PIC64GX1000-KIT-ES](https://www.microchip.com/en-us/development-tool/CURIOSITY-PIC64GX1000-KIT-ES) (PIC64GX Curiosity Kit)

## Build Instructions

Before continuing, ensure that the prerequisite packages are present on your system. Please see the [Host PC setup for Yocto section](#Dependencies) for further details.

### Switch bash

It might be required for you to switch shell to bash, there is a known issue
with zsh
```bash
$ chsh -s /bin/bash
```

### Create the Workspace

This needs to be done every time you want a clean setup based on the latest BSP.

```bash
$ mkdir yocto-dev && cd yocto-dev
$ repo init -u https://github.com/pic64gx/pic64gx-yocto-manifests.git -b main -m default.xml
```

### Update the repo workspace

```bash
repo sync
repo rebase
```

### Setup Bitbake environment

```bash
. ./meta-pic64gx-yocto-bsp/pic64gx_yocto_setup.sh
```

### Building board Disk Image

#### Building a Linux Image with a root file system (RootFS)

Using Yocto bitbake command and setting the MACHINE and image required.

```bash
MACHINE=pic64gx-curiosity-kit bitbake pic64gx-dev-cli
```

For instructions on how to copy the image to the eMMC or SD card refer to the [Copy the created Disk Image to a flash device](#Copy-the-created-Disk-Image-to-flash-device) section.

#### Building a RAM-based Root Filesystem (Initramfs)

Using Yocto bitbake command and setting the initramfs configuration file (conf/initramfs.conf) and the pic64gx-initramfs-image

```bash
MACHINE=pic64-gx-curiosity-kit bitbake -R conf/initramfs.conf pic64gx-initramfs-image
```

The image generated from the command above can be used to boot Linux with a RAM-based root filesystem from the eMMC or SD card.

For instructions on how to copy the image to the eMMC or SD card refer to the [Copy the created Disk Image to a flash device](#Copy-the-created-Disk-Image-to-flash-device) section.

<a name="Copy-the-created-Disk-Image-to-flash-device"></a>

### Copy the created Disk Image to flash device (USB mmc flash/SD/uSD)

> Be very careful while picking /dev/sdX device! Look at dmesg, lsblk, GNOME Disks, etc. before and after plugging in your usb flash device/uSD/SD to find a proper device. Double check it to avoid overwriting any of system disks/partitions!
>

We recommend using the `bmaptool` utility to program the storage device. `bmaptool` is a generic tool for creating the block map (bmap) for a file and copying files using this block map. Raw system image files can be flashed a lot faster with bmaptool than with traditional tools, like "dd" or "cp".

The created disk image is a 'wic' file, and is located in `yocto-dev/build/tmp-glibc/deploy/images/<MACHINE>/` directory,

e.g., for pic64gx-dev-cli image and machine pic64-gx-curiosity-kit, it will be located in

`yocto-dev/build/tmp-glibc/deploy/images/pic64-gx-curiosity-kit/pic64gx-dev-cli-pic64-gx-curiosity-kit.wic`.

```bash
cd yocto-dev/build
bmaptool copy tmp-glibc/deploy/images/pic64-gx-curiosity-kit/pic64gx-dev-cli-pic64-gx-curiosity-kit.wic /dev/sdX
```

The wic image uses a GUID Partition Table (GPT). GPT stores its primary GPT header at the start of the storage device, and a secondary GPT header at the end of the device.  The wic creation scripts do not correctly place this secondary GPT header at the current time.  To avoid later warnings about the GPT secondary header location, open the device with fdisk at this stage and rewrite the partition table:

```bash
fdisk /dev/sdX
```

This will output something like the following:

```bash
Welcome to fdisk (util-linux 2.34).
Changes will remain in memory only, until you decide to write them.
Be careful before using the write command.

GPT PMBR size mismatch (13569937 != 15273599) will be corrected by write.
The backup GPT table is not on the end of the device. This problem will be corrected by write.

Command (m for help):
```

Press `w` to write the partition table and exit `fdisk`:

```bash
Command (m for help): w

The partition table has been altered.
Calling ioctl() to re-read partition table.
Syncing disks.
```

### Supported Machine Targets

The `MACHINE` (board) option can be used to set the target board for which linux is built, and if left blank it will default to `MACHINE=pic64-gx-curiosity-kit`.
The following table details the available targets:

| `MACHINE`                          | Board Name                                                                                |
| ---------------------------------- | ----------------------------------------------------------------------------------------- |
| `MACHINE=pic64gx-curiosity-kit`    | CURIOSITY-PIC64GX1000-KIT-ES, PIC64GX Curiosity Kit                                       |
| `MACHINE=pic64gx-curiosity-kit-amp`| CURIOSITY-PIC64GX1000-KIT-ES, PIC64GX Curiosity Kit in AMP mode                           |

The `pic64-gx-curiosity-kit-amp` machine can be used to build the PIC64GX Curiosity kit with AMP support. Please see the [Asymmetric Multiprocessing (AMP)](https://mi-v-ecosystem.github.io/redirects/asymmetric-multiprocessing_amp) documentation for further details.

The `pic64-gx-curiosity-kit-auth` machine can be used to build an image that demonstrates a simple approach
for booting an authenticated Linux kernel. Please see the [Linux Boot Authentication](https://mi-v-ecosystem.github.io/redirects/linux-boot-authentication) documentation for further details on
how to build an authentication scheme implementing a chain of trust.

When building for different 'Machines' or want a 'clean' build, we recommend deleting the 'build' directory when switching.
This will delete all cache / configurations and downloads.

```bash
cd yocto-dev
rm -rf build
```

### Linux Images

The table below summarizes the most common Linux images that can be built using this BSP.

| bitbake `<image>` argument    | Description                                                                  |
| ------------------------------| -----------------------------------------------------------------------------|
| `core-image-minimal-dev`      | A small console image with some development tools.                           |
| `core-image-minimal-mtdutils` | A small image with minimal MTD utilities to interact with QSPI flash devices |
| `pic64gx-dev-cli`             | A console image with development tools.                                      |
| `pic64gx-initramfs-image`     | A small RAM-based Root Filesystem (initramfs) image                          |

For more information on available images refer to [Yocto reference manual](https://docs.yoctoproject.org/ref-manual/images.html)

#### Target machine Linux login

Login with `root` account, there is no password set.

## Bitbake commands

With the bitbake environment setup, execute the bitbake command in the following format to build the disk images.

```bash
MACHINE=<machine> bitbake <image>
```

Example building the pic64-gx-curiosity-kit machine and the pic64gx-dev-cli Linux image

```bash
MACHINE=pic64-gx-curiosity-kit bitbake pic64gx-dev-cli
```

To work with individual recipes:

```bash
MACHINE=<MACHINE> bitbake <recipe> -c <command>
```

View/Edit the Kernel menuconfig:

```bash
MACHINE=<MACHINE> bitbake pic64gx-linux -c menuconfig
```

Run the diffconfig command to prepare a configuration fragment.
The resulting file fragment.cfg should be copied to meta-pic64gx-yocto-bsp/recipes-kernel/linux/files directory:
Afterwards the pic64gx-linux.bb src_uri should be updated to include the <fragment>.cfg,

```bash
MACHINE=<MACHINE> bitbake pic64gx-linux -c diffconfig
```

**Available BSP recipes:**

- hss (Microchip Hart Software Services) payload generator
- u-boot-mchp (Microchip U-Boot)
- pic64gx-linux (Linux Kernel for PIC64GX)

**Available commands:**

- clean
- configure
- compile
- install

### Yocto Image and Binaries directory

```bash
build/tmp-glibc/deploy/images/{MACHINE}
```

For Example the following is the path for the pic64-gx-curiosity-kit

```bash
build/tmp-glibc/deploy/images/pic64-gx-curiosity-kit
```

<a name="Dependencies"></a>
## Host PC setup for Yocto

### Yocto Dependencies

This document assumes you are running on a modern Linux system. The process documented here was tested using Ubuntu 20.04 LTS,
Debian 11 and have been reported to work with WSL2 on Windows 11.
It should also work with other Linux distributions if the equivalent prerequisite packages are installed.

The BSP uses the Yocto RISCV Architecture Layer, and the Yocto release Kirkstone (Revision 4.0.19) (Released June 2024).

**Make sure to install the [repo utility](https://source.android.com/setup/develop#installing-repo) first.**

Detailed instructions for various distributions can be found in the ["Required Packages for the Build Host"](https://docs.yoctoproject.org/4.0.19/ref-manual/system-requirements.html#required-packages-for-the-build-host) section in the Yocto Project Reference Manual.

```bash
**Note: Some extra packages are requried to support the Yocto 4.0.19 Release (codename “kirkstone”) compared to the prior release.**
```

<a name="OtherDeps"></a>
### Other Dependencies

For Ubuntu 18.04 (or newer) install python3-distutils:

```bash
sudo apt install python3-distutils
```

You can install the bmap-tools package using the following command:

```bash
sudo apt-get install bmap-tools
```

## Additional Reading

[Yocto Overview Manual](https://docs.yoctoproject.org/overview-manual/index.html)

[Yocto Development Task Manual](https://docs.yoctoproject.org/dev-manual/index.html)

[Yocto Bitbake User Manual](https://docs.yoctoproject.org/bitbake/index.html)

[Yocto Application Development and Extensible Software Development Kit (sSDK)](https://docs.yoctoproject.org/sdk-manual/index.html)

[Linux4Microchip Buildroot External for PIC64GX](https://github.com/linux4microchip/buildroot-external-microchip)

[U-Boot Documentation](https://www.denx.de/wiki/U-Boot/Documentation)

[Kernel Documentation for v6.6](https://www.kernel.org/doc/html/v6.6/)

[Yocto Flashing images using bmaptool](https://www.yoctoproject.org/docs/current/mega-manual/mega-manual.html#flashing-images-using-bmaptool)

## Licensing

This project is licensed under the terms of the MIT license (please see LICENSE file in this directory for further details).
By using the PIC64GX Yocto BSP layer in this repository, the user agrees to the terms and conditions from the licenses of the packages that are installed into the final image and that are covered by a commercial license.
The user also acknowledges that it's their responsibility to make sure they hold the right to use code protected by commercial agreements, whether the commercially protected packages are selected by Microchip's PIC64GX BSPs or by them.
Finally, the user acknowledges that it's their responsibility to make sure they hold the right to copy, use, modify, and re-distribute the intellectual property offered by this collection of meta-layers.

## Known issues

### Issue 001: Required binaries not available before creating the disk image

We sometimes get dependencies not building correctly.
During the process do_wic_install payload may not be present.

For example after requesting a complete build:

```bash
MACHINE=pic64-gx-curiosity-kit bitbake pic64gx-dev-cli
```

If u-boot or boot.src.uimg or payload.bin is missing,
execute the following:

```bash
MACHINE=pic64-gx-curiosity-kit bitbake u-boot -c clean
MACHINE=pic64-gx-curiosity-kit bitbake u-boot -c install
```

And finally a complete build:

```bash
MACHINE=pic64-gx-curiosity-kit bitbake pic64gx-dev-cli
```

### Issue 002 fs.inotify.max_user_watches

Listen uses inotify by default on Linux to monitor directories for changes. It's not uncommon to encounter a system limit on the number of files you can monitor. For example, Ubuntu Lucid's (64bit) inotify limit is set to 8192.

You can get your current inotify file watch limit by executing:

```bash
$ cat /proc/sys/fs/inotify/max_user_watches
```

When this limit is not enough to monitor all files inside a directory, the limit must be increased for Listen to work properly.

Run the following command:

```bash
echo fs.inotify.max_user_watches=524288 | sudo tee -a /etc/sysctl.conf && sudo sysctl -p
```

[Details on max_user_watches](https://github.com/guard/listen/wiki/Increasing-the-amount-of-inotify-watchers)

See [Other Dependencies](#OtherDeps) for installation instructions.
