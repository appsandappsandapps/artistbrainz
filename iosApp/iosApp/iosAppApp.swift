import SwiftUI
import shared

@main
struct iOSApp: App {
    var body: some Scene {
        WindowGroup {
            let _ = ServiceLocator()
            ContentView().onAppear {
                let uitab = UITabBarItemAppearance()
                uitab.normal.badgeBackgroundColor = .systemBlue
                uitab.selected.badgeBackgroundColor = .systemBlue
                let appearance = UITabBarAppearance()
                appearance.stackedLayoutAppearance = uitab
                appearance.inlineLayoutAppearance = uitab
                appearance.compactInlineLayoutAppearance = uitab
                UITabBar.appearance().standardAppearance = appearance

            }
        }
    }
}
