# do mtd last
IMAGE_TYPEDEP:mtd += "${IMAGE_FSTYPES}"
IMAGE_TYPEDEP:mtd:remove = "mtd"

IMAGE_INSTALL += "kernel u-boot-pic64gx"
EXTRA_IMAGE_FEATURES:remove = "package-management ssh-server-openssh"
