DESCRIPTION = "Microchip PIC64GX Initramfs image \
This recipe wraps an initramfs kernel into a WIC image"

DEPENDS += "pic64gx-core-image-base"
do_image_wic[depends] += "pic64gx-core-image-base:do_image_complete"

LICENSE = "MIT"

WKS_FILE_DEPENDS:append ?= " \
    pic64gx-core-image-base \
"

WKS_FILE = "pic64gx-initramfs.wks"

IMAGE_INSTALL = ""

IMAGE_FSTYPES:remove = " ext4"

do_rootfs[depends] += "pic64gx-core-image-base:do_image_complete"

inherit core-image

python do_check_initramfs_image () {
    initramfs_image = d.getVar("INITRAMFS_IMAGE", True)

    if initramfs_image is None or initramfs_image != "pic64gx-core-image-base":
        bb.error("INITRAMFS_IMAGE = \"pic64gx-core-image-base\" not set")
        bb.fatal("Use bitbake -R conf/initramfs.conf pic64gx-initramfs-image to build an Initramfs WIC image ")
}
addtask check_initramfs_image before do_rootfs