#!/usr/bin/env python3
from pathlib import Path
from PIL import Image

SOURCE = Path("/sdcard/Download/SpriteSheets")
DEST = Path("app/src/main/assets/spritesheets")
QUALITY = 85

FILES = {
    "Block-.png": "Block",
    "BlockHit-.png": "BlockHit",
    "CrouchDown-.png": "CrouchDown",
    "CrouchIdle-.png": "CrouchIdle",
    "CrouchUp-.png": "CrouchUp",
    "Fall-.png": "Fall",
    "Hurt-.png": "Hurt",
    "Idle.png": "Idlespritesheet",
    "Jab-.png": "Jab",
    "JumpRise-.png": "JumpRise",
    "JumpStart-.png": "JumpStart",
    "KameCharge-.png": "KameCharge",
    "KameChargeRelease-.png": "KameChargeRelease",
    "KameFire-.png": "KameFire",
    "KameRelease-.png": "KameRelease",
    "KnockedOut-.png": "KnockedOut",
    "Land-.png": "Land",
    "VFXKameCharge.png": "VFXKameCharge",
    "VFXKameEnd.png": "VFXKameEnd",
    "VFXKameImpact-.png": "VFXKameImpact",
    "VFXKameMid.png": "VFXKameMid",
    "VFXKameStart.png": "VFXKameStart",
    "Walk-.png": "Walk",
}

PNG_FILES = {
    "VFXKameCharge",
    "VFXKameEnd",
    "VFXKameImpact",
    "VFXKameMid",
    "VFXKameStart",
}


def main():
    DEST.mkdir(parents=True, exist_ok=True)
    for old in DEST.glob("*.png"):
        old.unlink()
    for old in DEST.glob("*.jpg"):
        old.unlink()

    total_source = 0
    total_dest = 0
    for source_name, dest_name in FILES.items():
        source = SOURCE / source_name
        total_source += source.stat().st_size
        if dest_name in PNG_FILES:
            dest = DEST / f"{dest_name}.png"
            with Image.open(source) as image:
                image.save(dest, format="PNG", optimize=True, compress_level=9)
        else:
            dest = DEST / f"{dest_name}.jpg"
            with Image.open(source) as image:
                image.convert("RGB").save(dest, format="JPEG", quality=QUALITY, optimize=True, progressive=True)
        total_dest += dest.stat().st_size
        print(f"{source.name} -> {dest.name}: {source.stat().st_size} -> {dest.stat().st_size}")

    print(f"total: {total_source} -> {total_dest}, saved {total_source - total_dest}")


if __name__ == "__main__":
    main()
