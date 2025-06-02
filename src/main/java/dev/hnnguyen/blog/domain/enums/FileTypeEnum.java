package dev.hnnguyen.blog.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum FileTypeEnum implements CodeEnum {
    PDF("PDF", ""),
    TXT("TXT", ""),
    DOC("DOC", ""),
    DOCX("DOCX", ""),
    PPT("PPT", ""),
    PPTX("PPTX", ""),
    XLS("XLS", ""),
    XLSX("XLSX", ""),
    PNG("PNG", ""),
    JPEG("JPEG", ""),
    JPG("JPG", ""),
    BMP("BMP", ""),
    DIB("DIB", ""),
    PCP("PCP", ""),
    DIF("DIF", ""),
    WMF("WMF", ""),
    GIF("GIF", ""),
    TIF("TIF", ""),
    EPS("EPS", ""),
    PSD("PSD", ""),
    CDR("CDR", ""),
    IFF("IFF", ""),
    TGA("TGA", ""),
    PCD("PCD", ""),
    MPT("MPT", ""),
    MP4("MP4", ""),
    MOV("MOV", ""),
    AVI("AVI", ""),
    SRT("SRT", ""),
    VTT("VTT", ""),
    HEIF("HEIF", ""),
    WEBP("WEBP", ""),
    HEIC("HEIC", ""),
    SVG("SVG", ""),
    ICO("ICO", ""),
    CVS("CVS", ""),
    CSV("CSV", ""),
    TIFF("TIFF", "");

    private final String value;
    private final String name;
}
