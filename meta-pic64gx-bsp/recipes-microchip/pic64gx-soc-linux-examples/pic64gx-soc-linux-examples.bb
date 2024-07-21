SUMMARY = "PIC64gx SoC Linux example applications"
DESCRIPTION = "Linux Example applications"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${WORKDIR}/git/LICENSE;md5=06ec214e9fafe6d4515883d77674a453"

DEPENDS = "collectd libgpiod  python3-flask"
inherit systemd

PV = "1.0+git${SRCPV}"
SRCREV="ff749ffb982b601f2cc99a53566511711dca65f5"
SRC_URI = "git://github.com/pic64gx/pic64gx-linux-examples.git;protocol=https;nobranch=1 \
          "

S = "${WORKDIR}/git"

EXAMPLE_FILES:pic64gx-curiosity-kit = "\
    dt-overlays \
    multimedia \
    "

EXAMPLE_FILES:pic64gx-curiosity-kit-amp = "\
    dt-overlays \
    amp/rpmsg-pingpong \
    amp/rpmsg-tty-example \
    "

do_compile() {
    for i in ${EXAMPLE_FILES}; do
        if [ -f ${S}/$i/Makefile ]; then
        oe_runmake -C ${S}/$i
        fi
    done
}

INSANE_SKIP_${PN} += "file-rdeps"
INSANE_SKIP:${PN} = "ldflags"
INSANE_SKIP:${PN}-dev = "ldflags"

SECURITY_CFLAGS = ""

do_install() {
    install -d ${D}/opt/microchip
    chmod a+x ${D}/opt/microchip

    for i in ${EXAMPLE_FILES}; do
        install -d ${D}/opt/microchip/`dirname $i`/`basename $i`
        cp -rfd ${S}/$i ${D}/opt/microchip/`dirname $i`
        done
}

FILES:${PN} += "/opt/microchip/"

