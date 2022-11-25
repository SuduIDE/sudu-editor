package org.sudu.experiments.win32;

@SuppressWarnings("SpellCheckingInspection")
public interface WindowPeer {
  JHandle<WindowPeer> peers = new JHandle<>(new WindowPeer[0]);

  int WM_CREATE               = 0x0001;
  int WM_DESTROY              = 0x0002;
  int WM_MOVE                 = 0x0003;
  int WM_SIZE                 = 0x0005;
  int WM_ACTIVATE             = 0x0006;
  int WM_SETFOCUS             = 0x0007;
  int WM_KILLFOCUS            = 0x0008;
  int WM_PAINT                = 0x000F;
  int WM_CLOSE                = 0x0010;
  int WM_SHOWWINDOW           = 0x0018;
  int WM_ACTIVATEAPP          = 0x001C;
  int WM_SETCURSOR            = 0x0020;
  int WM_GETMINMAXINFO        = 0x0024;
  int WM_WINDOWPOSCHANGING    = 0x0046;
  int WM_WINDOWPOSCHANGED     = 0x0047;
  int WM_GETICON              = 0x007F;
  int WM_SETICON              = 0x0080;

  int WM_KEYDOWN              = 0x0100;
  int WM_KEYUP                = 0x0101;
  int WM_CHAR                 = 0x0102;
  int WM_SYSKEYDOWN           = 0x0104;
  int WM_SYSKEYUP             = 0x0105;
  int WM_SYSCHAR              = 0x0106;

  int WM_SYSCOMMAND           = 0x0112;
  int SC_MAXIMIZE = 0xF030;

  int WM_TIMER                = 0x0113;

  int WM_MENUGETOBJECT        = 0x0124;
  int WM_UNINITMENUPOPUP      = 0x0125;
  int WM_MOUSEMOVE            = 0x0200;
  int WM_LBUTTONDOWN          = 0x0201;
  int WM_LBUTTONUP            = 0x0202;
  int WM_LBUTTONDBLCLK        = 0x0203;
  int WM_RBUTTONDOWN          = 0x0204;
  int WM_RBUTTONUP            = 0x0205;
  int WM_RBUTTONDBLCLK        = 0x0206;
  int WM_MBUTTONDOWN          = 0x0207;
  int WM_MBUTTONUP            = 0x0208;
  int WM_MBUTTONDBLCLK        = 0x0209;
  int WM_MOUSEWHEEL           = 0x020A;
  int WM_MOUSELAST            = 0x020E;

  int WM_ENTERSIZEMOVE        = 0x0231;
  int WM_EXITSIZEMOVE         = 0x0232;

  int WM_TOUCH                = 0x0240;

  // WM_SIZE args
  int SIZE_MINIMIZED = 1;
  int SIZE_MAXIMIZED = 2;

  long windowProc(long hWnd, int msg, long wParam, long lParam);

  static String wmToString(int wm) {
    return switch (wm) {
      case WM_CREATE -> "WM_CREATE";
      case WM_DESTROY -> "WM_DESTROY";
      case WM_MOVE -> "WM_MOVE";
      case WM_SIZE -> "WM_SIZE";
      case WM_ACTIVATE -> "WM_ACTIVATE";
      case WM_SETFOCUS -> "WM_SETFOCUS";
      case WM_KILLFOCUS -> "WM_KILLFOCUS";
      case WM_SETCURSOR -> "WM_SETCURSOR";
      case WM_GETMINMAXINFO -> "WM_GETMINMAXINFO";
      case WM_PAINT -> "WM_PAINT";
      case WM_CLOSE -> "WM_CLOSE";
      case WM_SHOWWINDOW -> "WM_SHOWWINDOW";
      case WM_ACTIVATEAPP -> "WM_ACTIVATEAPP";
      case WM_MENUGETOBJECT -> "WM_MENUGETOBJECT";
      case WM_UNINITMENUPOPUP -> "WM_UNINITMENUPOPUP";
      case WM_MOUSEMOVE -> "WM_MOUSEMOVE";
      case WM_WINDOWPOSCHANGING -> "WM_WINDOWPOSCHANGING";
      case WM_WINDOWPOSCHANGED -> "WM_WINDOWPOSCHANGED";
      case WM_GETICON -> "WM_GETICON";
      case WM_SETICON -> "WM_SETICON";
      case WM_SYSCOMMAND -> "WM_SYSCOMMAND";
      case WM_TIMER -> "WM_TIMER";
      case WM_ENTERSIZEMOVE -> "WM_ENTERSIZEMOVE";
      case WM_EXITSIZEMOVE -> "WM_EXITSIZEMOVE";
      case WM_TOUCH -> "WM_TOUCH";
      default -> "WM_".concat(Integer.toHexString(wm));
    };
  }
}
