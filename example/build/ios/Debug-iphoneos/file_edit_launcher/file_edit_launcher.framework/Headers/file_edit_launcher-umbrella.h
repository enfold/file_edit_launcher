#ifdef __OBJC__
#import <UIKit/UIKit.h>
#else
#ifndef FOUNDATION_EXPORT
#if defined(__cplusplus)
#define FOUNDATION_EXPORT extern "C"
#else
#define FOUNDATION_EXPORT extern
#endif
#endif
#endif

#import "FileEditLauncherPlugin.h"

FOUNDATION_EXPORT double file_edit_launcherVersionNumber;
FOUNDATION_EXPORT const unsigned char file_edit_launcherVersionString[];

