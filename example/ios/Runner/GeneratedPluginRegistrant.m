//
//  Generated file. Do not edit.
//

#import "GeneratedPluginRegistrant.h"

#if __has_include(<file_edit_launcher/FileEditLauncherPlugin.h>)
#import <file_edit_launcher/FileEditLauncherPlugin.h>
#else
@import file_edit_launcher;
#endif

#if __has_include(<path_provider/FLTPathProviderPlugin.h>)
#import <path_provider/FLTPathProviderPlugin.h>
#else
@import path_provider;
#endif

@implementation GeneratedPluginRegistrant

+ (void)registerWithRegistry:(NSObject<FlutterPluginRegistry>*)registry {
  [FileEditLauncherPlugin registerWithRegistrar:[registry registrarForPlugin:@"FileEditLauncherPlugin"]];
  [FLTPathProviderPlugin registerWithRegistrar:[registry registrarForPlugin:@"FLTPathProviderPlugin"]];
}

@end
