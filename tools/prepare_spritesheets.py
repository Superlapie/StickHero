#!/usr/bin/env python3
from pathlib import Path
from collections import deque
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
    "EarthSmash.png": "EarthSmash",
    "BladeFlurry.png": "BladeFlurry",
    "ElectroPulse.png": "ElectroPulse",
    "FlameBurst.png": "FlameBurst",
    "FlashStep.png": "FlashStep",
}

PNG_FILES = {
    "VFXKameCharge",
    "VFXKameEnd",
    "VFXKameImpact",
    "VFXKameMid",
    "VFXKameStart",
    "ElectroPulse",
    "FlameBurst",
    "FlashStep",
}

FRAME_TRIM_TOP = {
    "ElectroPulse": (4, 4, 5),
}

BACKGROUND_KEYED_PNGS = {
    "ElectroPulse": ((248, 3, 233), 15),
}

FORCE_REBUILD = set(BACKGROUND_KEYED_PNGS)


def main():
    DEST.mkdir(parents=True, exist_ok=True)

    total_source = 0
    total_dest = 0
    prepared = 0
    skipped = 0
    for source_name, dest_name in FILES.items():
        source = SOURCE / source_name
        total_source += source.stat().st_size
        if dest_name in PNG_FILES:
            dest = DEST / f"{dest_name}.png"
            if dest_name not in FORCE_REBUILD and is_prepared(source, dest):
                skipped += 1
                total_dest += dest.stat().st_size
                print(f"{source.name} -> {dest.name}: skipped")
                continue
            with Image.open(source) as image:
                image = apply_frame_trim(image, dest_name)
                image = remove_keyed_background(image, dest_name)
                image.save(dest, format="PNG", optimize=True, compress_level=9)
        else:
            dest = DEST / f"{dest_name}.jpg"
            if is_prepared(source, dest):
                skipped += 1
                total_dest += dest.stat().st_size
                print(f"{source.name} -> {dest.name}: skipped")
                continue
            with Image.open(source) as image:
                image = apply_frame_trim(image, dest_name)
                image.convert("RGB").save(dest, format="JPEG", quality=QUALITY, optimize=True, progressive=True)
        prepared += 1
        total_dest += dest.stat().st_size
        print(f"{source.name} -> {dest.name}: {source.stat().st_size} -> {dest.stat().st_size}")

    print(f"prepared={prepared} skipped={skipped} total: {total_source} -> {total_dest}, saved {total_source - total_dest}")


def is_prepared(source: Path, dest: Path) -> bool:
    return dest.exists() and dest.stat().st_mtime >= source.stat().st_mtime


def apply_frame_trim(image: Image.Image, dest_name: str) -> Image.Image:
    trim = FRAME_TRIM_TOP.get(dest_name)
    if trim is None:
        return image
    columns, rows, trim_pixels = trim
    result = image.convert("RGBA")
    transparent = (0, 0, 0, 0)
    for row in range(rows):
        top = row * result.height // rows
        bottom = min(top + trim_pixels, (row + 1) * result.height // rows)
        for col in range(columns):
            left = col * result.width // columns
            right = (col + 1) * result.width // columns
            for y in range(top, bottom):
                for x in range(left, right):
                    result.putpixel((x, y), transparent)
    return result


def remove_keyed_background(image: Image.Image, dest_name: str) -> Image.Image:
    background = BACKGROUND_KEYED_PNGS.get(dest_name)
    if background is None:
        return image

    key_color, tolerance = background
    result = image.convert("RGBA")
    pixels = result.load()
    width, height = result.size
    visited = bytearray(width * height)
    queue = deque()

    def enqueue(x: int, y: int) -> None:
        if 0 <= x < width and 0 <= y < height:
            queue.append((x, y))

    for x in range(width):
        enqueue(x, 0)
        enqueue(x, height - 1)
    for y in range(1, height - 1):
        enqueue(0, y)
        enqueue(width - 1, y)

    transparent = (0, 0, 0, 0)
    while queue:
        x, y = queue.popleft()
        index = y * width + x
        if visited[index]:
            continue
        visited[index] = 1

        r, g, b, a = pixels[x, y]
        if max(
            abs(r - key_color[0]),
            abs(g - key_color[1]),
            abs(b - key_color[2]),
        ) > tolerance:
            continue

        pixels[x, y] = transparent
        enqueue(x - 1, y)
        enqueue(x + 1, y)
        enqueue(x, y - 1)
        enqueue(x, y + 1)

    return result


if __name__ == "__main__":
    main()
