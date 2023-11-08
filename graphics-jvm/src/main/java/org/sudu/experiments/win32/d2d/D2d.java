package org.sudu.experiments.win32.d2d;

import org.sudu.experiments.CString;
import org.sudu.experiments.win32.Win32;

//@SuppressWarnings("unused")
public interface D2d {

  // d2d errors are in winerror.h
  int D2DERR_WRONG_STATE              = 0x88990001;
  int D2DERR_UNSUPPORTED_PIXEL_FORMAT = 0x88982F80;
  int WINCODEC_ERR_ALREADYLOCKED      = 0x88982F0D;

  // enum DXGI_FORMAT
  int DXGI_FORMAT_R8G8B8A8_UNORM      = 28;

  // enum D2D1_ALPHA_MODE
  int D2D1_ALPHA_MODE_UNKNOWN         = 0;
  int D2D1_ALPHA_MODE_PREMULTIPLIED   = 1;
  int D2D1_ALPHA_MODE_STRAIGHT        = 2;
  int D2D1_ALPHA_MODE_IGNORE          = 3;

  // D2D1_TEXT_ANTIALIAS_MODE
  int D2D1_TEXT_ANTIALIAS_MODE_DEFAULT   = 0;
  int D2D1_TEXT_ANTIALIAS_MODE_CLEARTYPE = 1;
  int D2D1_TEXT_ANTIALIAS_MODE_GRAYSCALE = 2;
  int D2D1_TEXT_ANTIALIAS_MODE_ALIASED   = 3;

  // https://learn.microsoft.com/en-us/windows/win32/api/dwrite/ne-dwrite-dwrite_pixel_geometry
  int DWRITE_PIXEL_GEOMETRY_FLAT = 0;
  int DWRITE_PIXEL_GEOMETRY_RGB  = 1;
  int DWRITE_PIXEL_GEOMETRY_BGR  = 2;

  // https://learn.microsoft.com/en-us/windows/win32/api/dwrite/ne-dwrite-dwrite_rendering_mode
  int DWRITE_RENDERING_MODE_DEFAULT           = 0;
  int DWRITE_RENDERING_MODE_ALIASED           = 1;
  int DWRITE_RENDERING_MODE_GDI_CLASSIC       = 2;
  int DWRITE_RENDERING_MODE_GDI_NATURAL       = 3;
  int DWRITE_RENDERING_MODE_NATURAL           = 4;
  int DWRITE_RENDERING_MODE_NATURAL_SYMMETRIC = 5;


  // https://learn.microsoft.com/en-us/windows/win32/api/dwrite/ne-dwrite-dwrite_font_weight
  int DWRITE_FONT_WEIGHT_THIN         = 100;
  int DWRITE_FONT_WEIGHT_EXTRA_LIGHT  = 200;
  int DWRITE_FONT_WEIGHT_LIGHT        = 300;
  int DWRITE_FONT_WEIGHT_SEMI_LIGHT   = 350;
  int DWRITE_FONT_WEIGHT_REGULAR      = 400;
  int DWRITE_FONT_WEIGHT_MEDIUM       = 500;
  int DWRITE_FONT_WEIGHT_SEMI_BOLD    = 600;
  int DWRITE_FONT_WEIGHT_BOLD         = 700;
  int DWRITE_FONT_WEIGHT_EXTRA_BOLD   = 800;
  int DWRITE_FONT_WEIGHT_BLACK        = 900;
  int DWRITE_FONT_WEIGHT_ULTRA_BLACK  = 950;

  int DWRITE_FONT_STYLE_NORMAL  = 0;
  int DWRITE_FONT_STYLE_OBLIQUE = 1;
  int DWRITE_FONT_STYLE_ITALIC  = 2;

  int DWRITE_FONT_STRETCH_ULTRA_CONDENSED  = 1;
  int DWRITE_FONT_STRETCH_EXTRA_CONDENSED  = 2;
  int DWRITE_FONT_STRETCH_CONDENSED        = 3;
  int DWRITE_FONT_STRETCH_SEMI_CONDENSED   = 4;
  int DWRITE_FONT_STRETCH_NORMAL           = 5;
  int DWRITE_FONT_STRETCH_SEMI_EXPANDED    = 6;
  int DWRITE_FONT_STRETCH_EXPANDED         = 7;
  int DWRITE_FONT_STRETCH_EXTRA_EXPANDED   = 8;
  int DWRITE_FONT_STRETCH_ULTRA_EXPANDED   = 9;

  static String errorToString(int errorCode) {
    return switch (errorCode) {
      case D2DERR_WRONG_STATE              -> "D2DERR_WRONG_STATE";
      case D2DERR_UNSUPPORTED_PIXEL_FORMAT -> "D2DERR_UNSUPPORTED_PIXEL_FORMAT";
      case WINCODEC_ERR_ALREADYLOCKED      -> "WINCODEC_ERR_ALREADYLOCKED";
      default -> Win32.errorToString(errorCode);
    };
  }

  interface Locale {
    char[] noLocale = CString.emptyCString();
    char[] en_us = CString.toChar16CString("en-us");
  }
}
