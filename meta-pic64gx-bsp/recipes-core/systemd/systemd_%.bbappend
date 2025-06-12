FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append = " file://0001-networkd-add-any-option-to-the-wait-online.patch \
                   file://50-root.conf"

FILES:${PN} += " ${sysconfdir}/repart.d/*"

do_install:append(){
    install -d ${D}${sysconfdir}/repart.d/
    install -m 0644 ${WORKDIR}/50-root.conf ${D}${sysconfdir}/repart.d/
}

PACKAGECONFIG:append := "\
    openssl \
    repart \
"
